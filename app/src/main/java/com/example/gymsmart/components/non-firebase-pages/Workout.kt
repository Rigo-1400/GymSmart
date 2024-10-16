package com.example.gymsmart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Button




@Composable
fun Workout(navController: NavController, workoutName: String, videoIds: List<String>) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("This is the $workoutName page!")
            // Optional: Loop through videoIds if you want to display each video
            videoIds.forEach { videoId ->
                Button(
                    onClick = { navController.navigate("videoPlayer/$videoId") }
                ) {
                    Text("Watch Video $workoutName")
                }
            }
        }
    }
}
