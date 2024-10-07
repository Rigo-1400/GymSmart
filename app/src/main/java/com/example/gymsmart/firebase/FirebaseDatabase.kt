package com.example.gymsmart.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

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