package com.example.gymsmart.components.workouts

import android.content.Context
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
import android.widget.DatePicker
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.gymsmart.firebase.WorkoutData
import com.example.gymsmart.showDatePicker
import java.util.Calendar
import java.util.Date

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
    var showSpinner by remember { mutableStateOf(true) }



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
                    showSpinner = false
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

            @Composable
            fun showDatePicker(context: Context) {
                val year: Int
                val month: Int
                val day: Int
                var selectedDate by remember { mutableStateOf<Long?>(null) }


                val calendar = Calendar.getInstance()
                year = calendar.get(Calendar.YEAR)
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)


                calendar.time = Date()

                val date = remember { mutableStateOf("") }

                val datePickerDialog = android.app.DatePickerDialog(
                    context,

                    { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                        date.value = "Workouts for selected date:" + "$dayOfMonth/$month/ $year"
                    }, year, month, day
                )


                val wrkout = println("Workouts for today: ")
                Column(

                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally )
                {
                    Text(text = "Open Calendar: ${date.value}",)

                    Button(onClick = { datePickerDialog.show() }) {
                        Text(text = "Open")
                    }

                }




            }






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
            if(!showSpinner) {
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
            } else CircularProgressIndicator()
        }
    }
}

