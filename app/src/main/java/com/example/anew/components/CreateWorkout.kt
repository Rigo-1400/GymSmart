package com.example.anew.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreateWorkout(navController: NavController) {
    val upperWorkouts = arrayOf("Chest", "Biceps", "Triceps", "Shoulders", "Lats")
    val lowerWorkouts = arrayOf("Hamstring", "Glutes", "Quadriceps", "Calves")
    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = { navController.navigate("workouts/${upperWorkouts.joinToString(",")}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Upper Body Workouts")
            }

            Button(
                onClick = { navController.navigate("workouts/${lowerWorkouts.joinToString(",")}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Lower Body Workouts")
            }
        }
    }
}