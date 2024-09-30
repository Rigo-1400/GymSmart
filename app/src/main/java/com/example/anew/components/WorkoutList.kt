package com.example.anew.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.navigation.NavController
import com.example.anew.firebase.WorkoutData
import com.google.gson.Gson

@Composable
fun WorkoutList(navController: NavController) {
    // Firestore and Auth instances
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    var workouts by remember { mutableStateOf(listOf<WorkoutData>()) }

    // Fetch workouts from Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("users").document(userId).collection("workouts")
                .get()
                .addOnSuccessListener { documents ->
                    val fetchedWorkouts = documents.mapNotNull { document ->
                        document.toObject(WorkoutData::class.java).also {
                            it.id = document.id
                        }
                    }
                    workouts = fetchedWorkouts
                }
                .addOnFailureListener { e ->
                    Log.w("WorkoutListPage", "Error getting documents", e)
                }
        }
    }

    // Split workouts into upper and lower body lists
    val upperBodyWorkouts = workouts.filter { it.partOfTheBody.equals("Upper Body", ignoreCase = false) }
    val lowerBodyWorkouts = workouts.filter { it.partOfTheBody.equals("Lower Body", ignoreCase = false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (upperBodyWorkouts.isNotEmpty()) {
                item {
                    Text(
                        text = "Upper Body Workouts",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(upperBodyWorkouts) { workout ->
                    WorkoutItem(workout, navController)
                }
            }

            // Display Lower Body Workouts
            if (lowerBodyWorkouts.isNotEmpty()) {
                item {
                    Text(
                        text = "Lower Body Workouts",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(lowerBodyWorkouts) { workout ->
                    WorkoutItem(workout, navController)
                }
            }
        }
    }
}

@Composable
fun WorkoutItem(workout: WorkoutData, navController: NavController) {
    val gson = Gson()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                val workoutJson = gson.toJson(workout)
                navController.navigate("workoutDetail/$workoutJson")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Workout: ${workout.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Muscle Group: ${workout.muscleGroup}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun WorkoutDetails(workoutData: WorkoutData?) {
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
            }
        }
    }
}




