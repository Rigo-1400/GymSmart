package com.example.gymsmart.components.pages.workouts


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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.gymsmart.components.ui.WorkoutDatePicker
import com.example.gymsmart.components.ui.FilterDropdownMenu
import com.example.gymsmart.components.ui.SearchBarWithIcon
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.components.ui.WorkoutItem
import com.example.gymsmart.firebase.WorkoutData
import com.example.gymsmart.firebase.FirebaseAuthHelper
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * UserWorkouts list
 *
 * @param navController
 */
@Composable
fun UserWorkoutsPage(
    navController: NavController,
    firebaseAuthHelper: FirebaseAuthHelper) {
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
                    workouts = fetchedWorkouts.sortedByDescending { it.dateAdded }
                    filteredWorkouts = workouts
                    showSpinner = false
                }
                .addOnFailureListener { e ->
                    Log.w("WorkoutListPage", "Error getting documents", e)
                }
        }
    }

    // Function to apply filters
    fun applyFilter(filterType: String) {
        filteredWorkouts = when (filterType) {
            "All" -> workouts
            "Sort by Date (Newest)" -> workouts.sortedByDescending { it.dateAdded }
            "Sort by Date (Oldest)" -> workouts.sortedBy { it.dateAdded }
            "Upper Body" -> workouts.filter { it.partOfTheBody.equals("Upper Body", ignoreCase = true) }
            "Lower Body" -> workouts.filter { it.partOfTheBody.equals("Lower Body", ignoreCase = true) }
            else -> workouts
        }
    }

    // Function to apply calendar filter
    fun applyCalendarFilter(selectedDate: String) {
        // Parse the selected date string from the calendar
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = dateFormat.parse(selectedDate)

        // Filter workouts by matching their dateAdded field (converted to Date)
        filteredWorkouts = workouts.filter { workout ->
            val workoutDate = workout.dateAdded.toDate()
            val workoutFormattedDate = dateFormat.format(workoutDate)
            workoutFormattedDate == parsedDate?.let { dateFormat.format(it) }
        }
    }


    // Function to handle the user setting navigation's
    fun navigateUserSettingMenu(setting: String) {
        when(setting) {
            "Settings" -> navController.navigate("settings")
            "Logout" -> navController.navigate("logout")
            else -> {
                Log.w("Navigation", "unknown setting: $setting")
            }
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
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // User Settings Drop Down Menu Button
                UserSettingsDropdownMenu({ setting -> navigateUserSettingMenu(setting) }, firebaseAuthHelper, navController )


                // Filter Button
                FilterDropdownMenu { filter -> applyFilter(filter) }

                // Calender Filter Button
                WorkoutDatePicker(LocalContext.current) { newDate -> applyCalendarFilter(newDate) }

                // Search Bar
                SearchBarWithIcon(searchQuery) { query -> searchQuery = query }
            }
            // Date Picker




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




