package com.example.anew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Workouts(navController: NavController, workouts: Array<String>) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            workouts.forEach { workoutName ->
                // This is where you define the list of videos for each workout
                val videoIds = when (workoutName) {
                    "Chest" -> listOf("dPb9JxFMuuE")
                    "Biceps" -> listOf("JyV7mUFSpXs")
                    "Triceps" -> listOf("JfSee0Q-vRQ")
                    "Shoulders" -> listOf("WQasM7Jh9dQ")
                    "Lats"-> listOf("VAvVpAABrTs")
                    "Hamstring" -> listOf("2PGC_gmgj30")
                    "Glutes" -> listOf("6hOAGDbkLOw")
                    "Quadriceps" -> listOf("KMnp7y6_sMA")
                    "Calves" -> listOf("qsRtp_PbVM")
                    else -> listOf("cbKkB3POqaY") // Default video if no specific workout is matched
                }.joinToString(",")

                Button(
                    onClick = {
                        navController.navigate("workout/$workoutName/$videoIds")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Go to $workoutName Workouts")
                }
            }
        }
    }
}
