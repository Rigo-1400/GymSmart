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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import com.composables.icons.lucide.Plus
import com.example.gymsmart.R
import com.example.gymsmart.components.ui.BottomNavigationBar
import com.example.gymsmart.components.ui.WorkoutDatePicker
import com.example.gymsmart.components.ui.FilterDropdownMenu
import com.example.gymsmart.components.ui.SearchBarWithIcon
import com.example.gymsmart.components.ui.WorkoutItem
import com.example.gymsmart.firebase.WorkoutData
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * UserWorkouts list
 *
 * @param navController
 */
@Composable
fun UserWorkoutsPage(navController: NavController) {
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
                    documents.mapNotNull{document ->
                        val workout = document.toObject(WorkoutData::class.java)
                        Log.d("FirestoreData", "Workout fetched: ${workout.isPR}")
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
        modifier = Modifier.fillMaxSize(),
        bottomBar = { navController.currentBackStackEntry?.destination?.route?.let { BottomNavigationBar(navController, it) } },
        floatingActionButton = {
            FloatingActionButtonWithMenu(navController)
        }
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
                //UserSettingsDropdownMenu({ setting -> navigateUserSettingMenu(setting) }, firebaseAuthHelper, navController )


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
    MyCustomDialog()
}

@Composable
fun FloatingActionButtonWithMenu(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(24.dp),

    ) {
        // Main Floating Action Button to expand/collapse the menu
        // Main Floating Action Button to expand/collapse the menu
        FloatingActionButton(
            onClick = { navController.navigate("workoutVideo") },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(Lucide.Play, contentDescription = "Create Workout")
        }
        FloatingActionButton(
            onClick = { navController.navigate("workoutCreator") },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(Lucide.Plus, contentDescription = "Create Workout")
        }
    }
}


@Composable
private fun MyCustomDialog() {
    var openAlert = remember {
        mutableStateOf(false)
    }


    Button(
        onClick = { openAlert.value = true },

        modifier = Modifier
            .width(200.dp)
            .height(100.dp),
        ) {
        Text(text = "Click me!")
    }

    if (openAlert.value) {
        CustomDialogUI(openAlert)
    }


}

@Composable
private fun CustomDialogUI(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox)
    }


}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>) {
    val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5
    )

    val randomImage = remember { mutableStateOf(images.random())}
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {

            Image(
                painter = painterResource(id = randomImage.value),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth()
            )

            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Congratulations!!", color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "You have earned a badge!", color = Color.Black,

                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp, 25.dp, 25.dp, 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium

                )


            }
            /** Buttons*/
            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                //     TextButton(onClick = { openDialog.value = false }) {
                //          Text(
                //             text = "Not now", fontWeight = FontWeight.Bold, color = Color.White,
                //                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                //           )
                //       }

                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Thanks!!", fontWeight = FontWeight.Bold, color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

            }


        }


    }

}

