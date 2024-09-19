package com.example.anew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
var workoutsB = arrayOf(
    "Hamstring",
    "Glutes",
    "Quadriceps",
    "Calves",
);

@Composable

fun WorkoutsB(navController: NavController) {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
        ) {
            workoutsB.forEach { workoutName2 ->
                Button(
                    onClick = { navController.navigate("workout/$workoutName2") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp) // Adjust padding for better visual appearance
                ) {
                    Text(workoutName2);
                }
            }
        }
    }
}