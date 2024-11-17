package com.example.gymsmart.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Save workout to firebase
 *
 * @param db
 * @param userId
 * @param dateAdded
 * @param partOfTheBody
 * @param workoutName
 * @param muscleGroup
 * @param sets
 * @param reps
 */
suspend fun saveWorkoutToFirebase(
    db: FirebaseFirestore,
    userId: String,
    dateAdded: Timestamp,
    partOfTheBody: String,
    workoutName: String,
    muscleGroup: String,
    sets: Int,
    reps: Int,
    weight: Int,
    isPR: Boolean
) {
    val workoutData = WorkoutData(
        dateAdded = dateAdded,
        partOfTheBody = partOfTheBody,
        name = workoutName,
        muscleGroup = muscleGroup,
        sets = sets,
        reps = reps,
        weight = weight,
        isPR = isPR
    )

    // Check for previous PR
    val (isNewPR, previousPRId) = checkForPR(workoutName, weight, reps, userId)

    if (isNewPR && previousPRId != null) {
        // Update the old PR entry's isPR field to false
        db.collection("users").document(userId)
            .collection("workouts").document(previousPRId)
            .update("isPR", false)
            .addOnSuccessListener {
                Log.d("PRCheck", "Old PR updated successfully (isPR set to false).")
            }
            .addOnFailureListener { e ->
                Log.w("PRCheck", "Failed to update old PR: $e")
            }
    }


    // Save the new workout (with the new PR if applicable)
    db.collection("users").document(userId)
        .collection("workouts")
        .add(workoutData)
        .addOnSuccessListener {
            Log.d("Firestore", "Workout saved successfully.")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Failed to save workout: $e")
        }
}


fun deleteWorkout(workoutId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid
    if (userID != null) {
        // Access the user's workouts collection and delete the workout with the specified ID
        db.collection("users")
            .document(userID)
            .collection("workouts")
            .document(workoutId)
            .delete()
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "Workout successfully deleted!")
                onSuccess() // Notify the caller that the deletion was successful
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseDatabase", "Error deleting workout", e)
                onFailure(e) // Notify the caller of the failure
            }
    } else {
        Log.w("FirebaseDatabase", "User is not logged in, cannot delete workout")
        onFailure(Exception("User not logged in"))
    }
}

fun updateWorkout(
    workoutId: String,
    userId: String,
    name: String,
    muscleGroup: String,
    sets: Int,
    reps: Int,
    weight: Int,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val workoutRef = FirebaseFirestore.getInstance()
        .collection("users")
        .document(userId)
        .collection("workouts")
        .document(workoutId)

    workoutRef.update(
        mapOf(
            "name" to name,
            "muscleGroup" to muscleGroup,
            "sets" to sets,
            "reps" to reps,
            "weight" to weight
        )
    ).addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}
suspend fun getPreviousWorkouts(workoutName: String, userId: String): List<WorkoutData> {
    val db = FirebaseFirestore.getInstance()
    val querySnapshot = db
        .collection("users")
        .document(userId)
        .collection("workouts")
        .whereEqualTo("name", workoutName)
        .get()
        .await()

    // Map the query results to your WorkoutData model
    val workoutList = querySnapshot.documents.map { doc ->
        WorkoutData(
            id = doc.id,
            dateAdded = doc.getTimestamp("dateAdded") ?: Timestamp.now(),
            partOfTheBody = doc.getString("partOfTheBody") ?: "",
            name = doc.getString("name") ?: "",
            muscleGroup = doc.getString("muscleGroup") ?: "",
            sets = doc.getLong("sets")?.toInt() ?: 0,
            reps = doc.getLong("reps")?.toInt() ?: 0,
            weight = doc.getLong("weight")?.toInt() ?: 0,
            isPR = doc.getBoolean("isPR") ?: false // Explicitly fetch as Boolean
        )
    }

    // Debug log to verify fetched data
    workoutList.forEach { workout ->
        Log.d("PRCheck", "Fetched workout: ${workout.name}, isPR: ${workout.isPR}")
    }

    return workoutList
}

suspend fun checkForPR(workoutName: String, weight: Int, reps: Int, userId: String): Pair<Boolean, String?> {
    // Fetch previous records for the given workout
    val previousRecords = getPreviousWorkouts(workoutName, userId)

    // If there are no previous records, return false (no PR check needed)
    if (previousRecords.isEmpty()) {
        return Pair(false, null)
    }

    // Find the previous max weight and corresponding PR ID
    val maxPreviousWeight = previousRecords.maxOfOrNull { it.weight } ?: 0
    val maxPreviousReps = previousRecords.filter { it.weight == maxPreviousWeight }.maxOfOrNull { it.reps } ?: 0
    val previousPRId = previousRecords.find { it.weight == maxPreviousWeight && it.reps == maxPreviousReps }?.id

    // Determine if the new entry is a PR based on the updated logic
    val isNewPR = when {
        // New weight is higher than the previous max weight
        weight > maxPreviousWeight -> true

        // Same weight, but more reps
        weight == maxPreviousWeight && reps > maxPreviousReps -> true

        // Any other case is not a PR
        else -> false
    }

    // Return whether it's a PR and the ID of the previous PR (if any)
    return Pair(isNewPR, previousPRId)
}




