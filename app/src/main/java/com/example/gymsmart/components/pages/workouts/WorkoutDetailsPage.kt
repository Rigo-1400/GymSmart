package com.example.gymsmart.components.pages.workouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymsmart.firebase.WorkoutData

/**
 * Workout details
 *
 * @param workoutData
 */
@Composable
fun WorkoutDetailsPage(workoutData: WorkoutData?) {
    // Display workout details
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        workoutData?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Workout Name: ${it.name}")
                Text("Muscle Group: ${it.muscleGroup}")
                Text("Sets: ${it.sets}")
                Text("Reps: ${it.reps}")
                Text("Weight: ${it.weight}")
            }
        }
    }
}
