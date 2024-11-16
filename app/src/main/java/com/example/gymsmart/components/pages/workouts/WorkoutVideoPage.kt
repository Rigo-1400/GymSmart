package com.example.gymsmart.components.pages.workouts

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MoveLeft
import com.example.gymsmart.BuildConfig
import com.example.gymsmart.api.searchYouTubeVideos
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.components.ui.YoutubePlayer
import com.example.gymsmart.firebase.FirebaseAuthHelper
import kotlinx.coroutines.launch

@Composable
fun WorkoutVideoPage(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    val apiKey = BuildConfig.GOOGLE_API_KEY
    var showVideoSpinner by remember { mutableStateOf(true) }
    var allVideos by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }
    var selectedWorkout by remember { mutableStateOf("All Workouts") }
    var expanded by remember { mutableStateOf(false) }

    val hardcodedWorkoutNames = listOf(
        "All Workouts", "Chest", "Triceps", "Biceps", "Shoulders",
        "Quadriceps", "Hamstrings", "Calves"
    )

    LaunchedEffect(hardcodedWorkoutNames) {
        showVideoSpinner = true
        allVideos = hardcodedWorkoutNames
            .filter { it != "All Workouts" }
            .associateWith { workoutName ->
                try {
                    searchYouTubeVideos(workoutName, apiKey)
                } catch (e: Exception) {
                    Log.e("WorkoutVideoPage", "Error fetching videos for $workoutName: ${e.message}")
                    emptyList()
                }
            }
        showVideoSpinner = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WorkoutTopBar(navController, firebaseAuthHelper)
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                WorkoutDropdownMenu(
                    selectedWorkout = selectedWorkout,
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    workoutNames = hardcodedWorkoutNames,
                    onWorkoutSelected = { selectedWorkout = it }
                )

                if (showVideoSpinner) {
                    LoadingSpinner()
                } else {
                    VideoList(allVideos = allVideos, selectedWorkout = selectedWorkout)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTopBar(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    TopAppBar(
        title = { Text("GymSmart", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Lucide.MoveLeft, contentDescription = "Move Back to Previous Page")
            }
        },
        actions = {
            UserSettingsDropdownMenu(
                { setting -> if (setting == "Settings") navController.navigate("settings") },
                firebaseAuthHelper = firebaseAuthHelper,
                navController
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1c1c1c)),
    )
}

@Composable
fun WorkoutDropdownMenu(
    selectedWorkout: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    workoutNames: List<String>,
    onWorkoutSelected: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = "Filter by Muscle Group: $selectedWorkout",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(true) }
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            workoutNames.forEach { workoutName ->
                DropdownMenuItem(
                    text = { Text(workoutName) },
                    onClick = {
                        onWorkoutSelected(workoutName)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingSpinner() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator()
        Text("Loading videos...")
    }
}

@Composable
fun VideoList(allVideos: Map<String, List<String>>, selectedWorkout: String) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val workoutsToShow = if (selectedWorkout == "All Workouts") {
            allVideos
        } else {
            mapOf(selectedWorkout to allVideos[selectedWorkout].orEmpty())
        }

        workoutsToShow.forEach { (workoutName, workoutVideoIds) ->
            item {
                Text(
                    text = workoutName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(workoutVideoIds, key = { it }) { videoId ->
                YoutubePlayer(videoId = videoId)
            }
        }
    }
}


