package com.example.gymsmart.firebase

import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.Timestamp

/**
 * Workout data
 *
 * @property id
 * @property dateAdded
 * @property partOfTheBody
 * @property name
 * @property muscleGroup
 * @property sets
 * @property reps
 * @constructor Create empty Workout data
 */
data class WorkoutData(
    var id: String = "",
    var dateAdded: Timestamp = Timestamp.now(),
    var partOfTheBody: String = "",
    var name: String = "",
    var muscleGroup: String = "",
    var sets: Int = 0,
    var reps: Int = 0,
    var weight: Int = 0
)
