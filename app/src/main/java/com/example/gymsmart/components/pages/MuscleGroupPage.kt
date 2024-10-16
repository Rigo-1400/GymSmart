package com.example.gymsmart.components.pages

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymsmart.components.WorkoutDatePicker
import com.example.gymsmart.firebase.WorkoutData

/**
 * Muscle group selector
 *
 * @param navController
 * @param workouts
 */
@Composable
fun MuscleGroupPage(navController: NavController, workouts: Array<String>) {

    // Creating two State variables
    val text by remember { mutableStateOf("") }


    // Filter the workouts based on the search query
    val filteredWorkouts = workouts.filter {
        it.contains(text, ignoreCase = true)
    }

    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
        ) {
            // Show filtered workouts below the search bar as buttons
            filteredWorkouts.forEach { muscleGroup ->
                val partOfTheBody = if(muscleGroup.startsWith("Chest") || muscleGroup.startsWith("Biceps") || muscleGroup.startsWith("Triceps") || muscleGroup.startsWith("Shoulders") || muscleGroup.startsWith("Lats")) {
                    "Upper Body"
                } else "Lower Body"
                Button(
                    onClick = { navController.navigate("workoutCreator/$partOfTheBody/$muscleGroup") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(muscleGroup)
                }
            }

        }
    }
}