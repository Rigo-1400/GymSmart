package com.example.gymsmart.components.workouts

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
import com.example.gymsmart.firebase.WorkoutData

/**
 * Workout list
 *
 * @param navController
 */
@Composable
fun UserWorkouts(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    var workouts by remember { mutableStateOf(listOf<WorkoutData>()) }
    var searchQuery by remember { mutableStateOf("") }

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

    // Filter workouts based on search query
    val filteredWorkouts = workouts.filter {
        it.name.contains(searchQuery, true) || it.muscleGroup.contains(searchQuery, true) || (it.partOfTheBody.contains(searchQuery, true))
    }

    // Filter upper and lower body workouts from the filtered workouts
    val filteredUpperBodyWorkouts = filteredWorkouts.filter { it.partOfTheBody.equals("Upper Body", true) }
    val filteredLowerBodyWorkouts = filteredWorkouts.filter { it.partOfTheBody.equals("Lower Body", true) }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { query ->
                    searchQuery = query
                },
                label = { Text("Search for a workout") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Display filtered Upper Body Workouts Header and Items
                if (filteredUpperBodyWorkouts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Upper Body Workouts",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(filteredUpperBodyWorkouts) { workout ->
                        WorkoutItem(workout, navController)
                    }
                }

                // Display filtered Lower Body Workouts Header and Items
                if (filteredLowerBodyWorkouts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Lower Body Workouts",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(filteredLowerBodyWorkouts) { workout ->
                        WorkoutItem(workout, navController)
                    }
                }

                // Show message if no workouts match the search
                if (filteredUpperBodyWorkouts.isEmpty() && filteredLowerBodyWorkouts.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Text(
                            text = "No workouts found",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}



