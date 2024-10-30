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
fun saveWorkoutToFirebase(
    db: FirebaseFirestore,
    userId: String,
    dateAdded: Timestamp,
    partOfTheBody: String,
    workoutName: String,
    muscleGroup: String,
    sets: Int,
    reps: Int,
    weight: Int
) {
    val workout = hashMapOf(
        "partOfTheBody" to partOfTheBody,
        "dateAdded" to dateAdded,
        "name" to workoutName,
        "muscleGroup" to muscleGroup,
        "sets" to sets,
        "reps" to reps,
        "weight" to weight
    )

    db.collection("users").document(userId).collection("workouts")
        .add(workout)
        .addOnSuccessListener { documentReference ->
            Log.d("WorkoutPage", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("WorkoutPage", "Error adding document", e)
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
suspend fun getWorkoutData(workoutId: String, userId: String): WorkoutData? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val document = db.collection("users")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .get()
            .await()

        if (document.exists()) {
            document.toObject(WorkoutData::class.java)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}