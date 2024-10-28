package com.example.gymsmart
import WorkoutDetailsPage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymsmart.components.pages.HomePage
import com.example.gymsmart.firebase.FirebaseAuthHelper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import com.example.gymsmart.components.pages.LoginPage
import com.example.gymsmart.components.pages.UserSettingsPage
import com.example.gymsmart.components.pages.workouts.UserWorkoutsPage
import com.example.gymsmart.components.pages.workouts.WorkoutCreatorPage
import com.example.gymsmart.firebase.WorkoutData
import com.google.gson.Gson

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create NavController in the setContent block
            val navController = rememberNavController()

            // Initialize com.example.gymsmart.firebase.FirebaseAuthHelper with the navController
            firebaseAuthHelper = FirebaseAuthHelper(this, navController, signInLauncher)

            val isDarkTheme = isSystemInDarkTheme()


            MaterialTheme(colorScheme = if(isDarkTheme) DarkColorScheme else LightColorScheme) {
                // Set up NavHost for navigation
                NavHost(navController = navController, startDestination = "login") {

                    // Login Page
                    composable("login") {
                        LoginPage(onGoogleSignInClick = {
                            firebaseAuthHelper.signIn() // Trigger Google Sign-In
                        })
                    }
                    // Home Page
                    composable("home") {
                        // Home Screen
                        HomePage(navController, firebaseAuthHelper)
                    }

                    // Workout Creator Page
                    composable("workoutCreator") { WorkoutCreatorPage(navController) }

                    // User Workouts Page
                    composable("workouts") { UserWorkoutsPage(navController)
                    }

                    // Workout Details Page
                    composable(
                        route = "workoutDetails/{workoutJson}",
                        arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                        val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                        WorkoutDetailsPage(workout)
                    }

                    // User Settings Page
                    composable("settings") { UserSettingsPage(navController, firebaseAuthHelper) }
                }
            }
        }
    }

    // In your Activity, define the launcher using the new Activity Result API
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result using the com.example.gymsmart.firebase.FirebaseAuthHelper
                firebaseAuthHelper.handleSignInResult(result.data)
            } else {
                Log.w("MainActivity", "Google sign-in canceled or failed")
            }
        }
}


