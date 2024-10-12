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
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.gymsmart.components.FilterDropdownMenu
import com.example.gymsmart.components.SearchBarWithIcon
import com.example.gymsmart.firebase.WorkoutData

/**
 * UserWorkouts list
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
    var showSpinner by remember { mutableStateOf(true) }
    var filteredWorkouts by remember { mutableStateOf(listOf<WorkoutData>()) }

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
                    filteredWorkouts = workouts
                    showSpinner = false
                }
                .addOnFailureListener { e ->
                    Log.w("WorkoutListPage", "Error getting documents", e)
                }
        }
    }

    // Function to apply filters
    fun applyFilter(filter: String) {
        filteredWorkouts = when (filter) {
            "All" -> workouts
            "Sort by Date (Newest)" -> workouts.sortedByDescending { it.dateAdded }
            "Sort by Date (Oldest)" -> workouts.sortedBy { it.dateAdded }
            "Upper Body" -> workouts.filter { it.partOfTheBody.equals("Upper Body", ignoreCase = true) }
            "Lower Body" -> workouts.filter { it.partOfTheBody.equals("Lower Body", ignoreCase = true) }
            else -> workouts
        }
    }

    // Filter workouts based on search query
    val displayedWorkouts = filteredWorkouts.filter {
        it.name.contains(searchQuery, true) ||
                it.muscleGroup.contains(searchQuery, true) ||
                it.partOfTheBody.contains(searchQuery, true)
    }

    // Separate the workouts into upper and lower body for display
    val upperBodyWorkouts = displayedWorkouts.filter { it.partOfTheBody.equals("Upper Body", ignoreCase = true) }
    val lowerBodyWorkouts = displayedWorkouts.filter { it.partOfTheBody.equals("Lower Body", ignoreCase = true) }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Filter Button
                FilterDropdownMenu(onFilterSelected = { filter -> applyFilter(filter) })

                // Search Bar
                SearchBarWithIcon(searchQuery) { query -> searchQuery = query }
            }

            if (showSpinner) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    // Display message if no workouts are found
                    if (displayedWorkouts.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Text(
                                text = "No workouts found",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    // Display Upper Body Workouts if any
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

                    // Display Lower Body Workouts if any
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

                    // Show message if no workouts exist at all
                    if (upperBodyWorkouts.isEmpty() && lowerBodyWorkouts.isEmpty() && searchQuery.isEmpty()) {
                        item {
                            Text("It seems you have no workouts to fetch from, please create a workout below.")
                            Button(
                                onClick = { navController.navigate("workoutCreator") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Create a Workout!")
                            }
                        }
                    }
                }
            }
        }
    }
}



