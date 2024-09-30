package com.example.anew.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.anew.firebase.saveWorkoutToFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Workout(navController: NavController, partOfTheBody: String, muscleGroup: String) {
    // State variables to track user input
    var workoutName by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }

    // Firestore and Auth instances
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
        ) {
            Text("This is the $muscleGroup page!")
            // Input for Workout Name
            TextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                label = { Text("Workout Name") },
                modifier = Modifier.padding(8.dp)
            )

            // Input for Sets
            TextField(
                value = sets,
                onValueChange = { sets = it },
                label = { Text("Sets") },
                modifier = Modifier.padding(8.dp)
            )

            // Input for Reps
            TextField(
                value = reps,
                onValueChange = { reps = it },
                label = { Text("Reps") },
                modifier = Modifier.padding(8.dp)
            )

            // Save Button
            Button(
                onClick = {
                    // Check if user is logged in and input is not empty before saving
                    if (userId != null && sets.isNotEmpty() && reps.isNotEmpty()) {
                        saveWorkoutToFirebase(db, userId, partOfTheBody, muscleGroup, workoutName, sets.toInt(), reps.toInt())
                        navController.navigate("workoutList")
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Save Workout")
            }
        }
    }
}