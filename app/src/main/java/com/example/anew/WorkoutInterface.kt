package com.example.anew

// Base interface defining a Workout with a single name property
interface Workout {
    val name: String
}

// Interface extending Workout, with a list of workout names
interface Workouts : Workout {
    // Array of workout names
    val workoutsNames: Array<String>

    // Override name to return the first workout name or default to "Unknown"
    override val name: String
        get() = workoutsNames.firstOrNull() ?: "Unknown"
}

// Class implementing the Workouts interface
class WorkoutInterface(override val workoutsNames: Array<String>) : Workouts