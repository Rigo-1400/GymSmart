package com.example.anew

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}
@Preview()
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Set up NavHost for navigation
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainPage(navController) }
        composable("workouts") { Workouts(navController) }
        composable("workoutsB") { WorkoutsB(navController) }
        composable("workout/{workoutName}") { navBackStackEntry ->
            val name = navBackStackEntry.arguments?.getString("workoutName")
            if (name != null) {
                Workout(name)
            }
        }
    }
}

@Composable
fun MainPage(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = { navController.navigate("workouts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Upper Body Workouts")
            }
            Button(
                onClick = { navController.navigate("workoutsB") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Lower Body Workouts")
            }
        }
    }
}
