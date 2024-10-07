package com.example.gymsmart.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun saveWorkoutToFirebase(
    db: FirebaseFirestore,
    userId: String,
    partOfTheBody: String,
    workoutName: String,
    muscleGroup: String,
    sets: Int,
    reps: Int
) {
    val workout = hashMapOf(
        "partOfTheBody" to partOfTheBody,
        "name" to workoutName,
        "muscleGroup" to muscleGroup,
        "sets" to sets,
        "reps" to reps
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

// Data class for workout information
data class WorkoutData(
    var id: String = "",
    var partOfTheBody: String = "",
    var name: String = "",
    var muscleGroup: String = "",
    var sets: Int = 0,
    var reps: Int = 0
)