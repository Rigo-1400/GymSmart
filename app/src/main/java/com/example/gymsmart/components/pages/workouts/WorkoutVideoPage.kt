package com.example.gymsmart.components.pages.workouts

import android.util.Log
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutVideoPage( navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    val apiKey = BuildConfig.GOOGLE_API_KEY
    var showVideoSpinner by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val hardcodedWorkoutNames = listOf(
        "Chest", "Triceps", "Biceps", "Shoulders",
        "Quadriceps", " Hamstrings", "Calves"
        )
    var workoutVideos by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

    LaunchedEffect(hardcodedWorkoutNames) {
        coroutineScope.launch {
            val videosMap = hardcodedWorkoutNames.associateWith { workoutName ->
                try {
                    searchYouTubeVideos(workoutName, apiKey)
                } catch (e: Exception) {
                    Log.e("WorkoutVideoPage", "Error fetching videos for $workoutName: ${e.message}")
                    emptyList()
                }
            }
            workoutVideos = videosMap
            showVideoSpinner = false
            Log.d("WorkoutVideoPage", "Fetched workout videos: $workoutVideos")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
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
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                if (showVideoSpinner) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                            Text("Loading videos...")
                        }
                    }
                } else {
                    workoutVideos.forEach { (workoutName, videoIds) ->
                        item {
                            Text(
                                text = workoutName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(videoIds) { videoId ->
                            YoutubePlayer(videoId = videoId)
                        }
                    }
                    if (workoutVideos.isEmpty()) {
                        item {
                            Text("No videos found for these exercises!")
                        }
                    }
                }
            }
        }
    )
}
