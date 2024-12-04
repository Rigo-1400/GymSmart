package com.example.gymsmart

import WorkoutDetailsPage
import com.example.gymsmart.components.pages.HomePage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymsmart.components.pages.*
import com.example.gymsmart.firebase.FirebaseAuthHelper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.navigation.navArgument
import com.example.gymsmart.DarkColorScheme
import com.example.gymsmart.LightColorScheme
import com.example.gymsmart.components.pages.workouts.UserWorkoutsPage
import com.example.gymsmart.components.pages.workouts.WorkoutCreatorPage
import com.example.gymsmart.components.pages.workouts.WorkoutVideoPage
import com.example.gymsmart.firebase.WorkoutData
import com.google.gson.Gson
import com.example.gymsmart.components.pages.CalorieCalculatorPage

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create NavController
            val navController = rememberNavController()

            // Initialize FirebaseAuthHelper
            firebaseAuthHelper = FirebaseAuthHelper(this, navController, signInLauncher)

            val isDarkTheme = isSystemInDarkTheme()

            MaterialTheme(colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme) {
                NavHost(navController = navController, startDestination = "login") {

                    // Login Page
                    composable("login") {
                        LoginPage(onGoogleSignInClick = {
                            firebaseAuthHelper.signIn()
                        })
                    }

                    // Home Page
                    composable("home") {
                        HomePage(navController, firebaseAuthHelper)
                    }

                    // Workout Creator Page
                    composable("workoutCreator") {
                        WorkoutCreatorPage(navController, firebaseAuthHelper)
                    }

                    // User Workouts Page
                    composable("workouts") {
                        UserWorkoutsPage(navController)
                    }

                    // Workout Video Page
                    composable("workoutVideo") {
                        WorkoutVideoPage(navController = navController, firebaseAuthHelper = firebaseAuthHelper)
                    }

                    // Workout Details Page
                    composable(
                        route = "workoutDetails/{workoutJson}",
                        arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                        val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                        WorkoutDetailsPage(workoutData = workout, navController = navController, firebaseAuthHelper)
                    }

                    // User Settings Page
                    composable("settings") { UserSettingsPage(navController, firebaseAuthHelper) }

                    // Attachments Page
                    composable("attachments") { AttachmentsPage(navController) }

                    // Calorie Calculator Page
                    composable("calorieCalculator") {
                        CalorieCalculatorPage(navController = navController)
                    }

                }
            }
        }
    }

    // Sign-In Launcher
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                firebaseAuthHelper.handleSignInResult(result.data)
            } else {
                Log.w("MainActivity", "Google sign-in canceled or failed")
            }
        }
}
