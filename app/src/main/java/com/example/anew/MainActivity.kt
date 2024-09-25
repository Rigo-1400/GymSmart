package com.example.anew

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

import com.example.anew.VideoPlayerActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    // Creating an object of the Workouts interface to pass down to the Workouts component.
    val navController = rememberNavController()

    // Set up NavHost for navigation
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainPage(navController) }

        // Define composable for workout screen, accepting a comma-separated list of workout names
        composable(
            route = "workouts/{workoutNames}",
            arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
            val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
            Workouts(navController, workoutNames)
        }

        composable(
            route = "workout/{workoutName}",
            arguments = listOf(navArgument("workoutName") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString("workoutName")
            name?.let { Workout(name) }
        }
    }
}

@Composable
fun MainPage(navController: NavController) {
    val upperWorkouts = arrayOf("Chest", "Biceps", "Triceps", "Shoulders", "Lats")
    val lowerWorkouts = arrayOf("Hamstring", "Glutes", "Quadriceps", "Calves")

    // Get the context to use with Intents
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = { navController.navigate("workouts/${upperWorkouts.joinToString(",")}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Upper Body Workouts")
            }
            Button(
                onClick = { navController.navigate("workouts/${lowerWorkouts.joinToString(",")}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Lower Body Workouts")
            }

            // New Button to launch VideoPlayerActivity
            Button(
                onClick = {
                    val intent = Intent(context, VideoPlayerActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Watch YouTube Video")
            }
        }
    }
}
