package com.example.anew

import com.example.anew.Workouts
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        // Main page
        composable("main") { MainPage(navController) }

        // Workouts screen
        composable(
            route = "workouts/{workoutNames}",
            arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
            val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
            Workouts(navController, workoutNames)
        }

        // Individual workout screen with list of video IDs
        composable(
            route = "workout/{workoutName}/{videoIds}",
            arguments = listOf(
                navArgument("workoutName") { type = NavType.StringType },
                navArgument("videoIds") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val workoutName = navBackStackEntry.arguments?.getString("workoutName")
            val videoIdsString = navBackStackEntry.arguments?.getString("videoIds")
            val videoIds = videoIdsString?.split(",") ?: listOf()
            if (workoutName != null) {
                Workout(navController, workoutName = workoutName, videoIds = videoIds)
            }
        }

        // VideoPlayer screen to launch the VideoPlayerActivity
        composable(
            route = "videoPlayer/{videoId}",
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val videoId = navBackStackEntry.arguments?.getString("videoId")
            VideoPlayerScreen(videoId)
        }
    }
}

@Composable
fun VideoPlayerScreen(videoId: String?) {
    val context = LocalContext.current

    androidx.compose.runtime.LaunchedEffect(Unit) {
        videoId?.let {
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_ID", videoId)
            }
            context.startActivity(intent)
        }
    }
}

@Composable
fun MainPage(navController: NavController) {
    val upperWorkouts = arrayOf("Chest", "Biceps", "Triceps", "Shoulders", "Lats")
    val lowerWorkouts = arrayOf("Hamstring", "Glutes", "Quadriceps", "Calves")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = {
                    navController.navigate("workouts/${upperWorkouts.joinToString(",")}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Upper Body Workouts")
            }
            Button(
                onClick = {
                    navController.navigate("workouts/${lowerWorkouts.joinToString(",")}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Lower Body Workouts")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
