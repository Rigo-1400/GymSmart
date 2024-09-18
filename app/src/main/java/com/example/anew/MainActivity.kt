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

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Set up NavHost for navigation
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainPage(navController) }
        composable("workoutA") { WorkoutAPage(navController) }
        composable("workoutB") { WorkoutBPage() }
        composable("randomPage") { RandomPage() }  // New Page Route
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
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                onClick = { navController.navigate("workoutA") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Workout A")
            }
            Button(
                onClick = { navController.navigate("workoutB") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Go to Workout B")
            }
        }
    }
}

@Composable
fun WorkoutAPage(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Workout A Page")
            Spacer(modifier = Modifier.height(16.dp))
            // Button to navigate to Random Page
            Button(
                onClick = { navController.navigate("randomPage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Random Page")
            }
        }
    }
}

@Composable
fun WorkoutBPage() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Workout B Page")
            // Add Workout B UI here
        }
    }
}

// New RandomPage Composable
@Composable
fun RandomPage() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Random Page")
            // Add UI for this random page here
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    MainPage(navController = rememberNavController())
}
