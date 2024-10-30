package com.example.gymsmart.components.pages.workouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymsmart.firebase.WorkoutData
import com.example.gymsmart.firebase.updateWorkout

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditWorkoutPage(navController: NavController, workoutData: WorkoutData, userId: String) {
    var name by remember { mutableStateOf(workoutData.name) }
    var muscleGroup by remember { mutableStateOf(workoutData.muscleGroup) }
    var sets by remember { mutableStateOf(workoutData.sets.toString()) }
    var reps by remember { mutableStateOf(workoutData.reps.toString()) }
    var weight by remember { mutableStateOf(workoutData.weight.toString()) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Workout Name") })
            TextField(value = muscleGroup, onValueChange = { muscleGroup = it }, label = { Text("Muscle Group") })
            TextField(value = sets, onValueChange = { sets = it }, label = { Text("Sets") })
            TextField(value = reps, onValueChange = { reps = it }, label = { Text("Reps") })
            TextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Update Firebase with the modified data
                    updateWorkout(
                        workoutId = workoutData.id,
                        userId = userId,
                        name = name,
                        muscleGroup = muscleGroup,
                        sets = sets.toInt(),
                        reps = reps.toInt(),
                        weight = weight.toInt(),
                        onSuccess = {
                            // Navigate back or show success message
                            navController.popBackStack()
                        },
                        onFailure = { e ->
                            // Handle error (e.g., show a message to the user)
                        }
                    )
                }
            ) {
                Text("Save Changes")
            }
        }
    }
}
