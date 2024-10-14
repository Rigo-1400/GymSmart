package com.example.gymsmart
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
import com.example.gymsmart.components.pages.MuscleGroupPage
import com.example.gymsmart.components.pages.PartOfBodyPage
import com.example.gymsmart.components.pages.UserSettingsPage
import com.example.gymsmart.components.pages.workouts.UserWorkoutsPage
import com.example.gymsmart.components.pages.workouts.WorkoutCreatorPage
import com.example.gymsmart.components.pages.workouts.WorkoutDetailsPage
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
                    composable("home") {
                        // Home Screen
                        HomePage(
                            navController = navController,
                            onSignOutClick = {
                                firebaseAuthHelper.signOut()
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true } // Clear backstack
                                }
                            }
                        )
                    }
                    // Define composable for workout screen, accepting a comma-separated list of workout names
                    composable(
                        route = "muscleGroupPage/{workoutNames}",
                        arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })
                    ) { navBackStackEntry ->
                        val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
                        val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                        MuscleGroupPage(navController, workoutNames)
                    }

                    composable(
                        route = "workoutCreator/{partOfTheBody}/{muscleGroup}",
                        arguments = listOf(navArgument("partOfTheBody") { type = NavType.StringType }, navArgument("muscleGroup") { NavType.StringType })
                    ) { navBackStackEntry ->
                        val pOfTheBody = navBackStackEntry.arguments?.getString("partOfTheBody")
                        val mGroup = navBackStackEntry.arguments?.getString("muscleGroup")
                        if (pOfTheBody != null && mGroup != null) {
                            WorkoutCreatorPage(navController, pOfTheBody, mGroup)
                        }
                    }
                    composable("login") {
                        // Login Screen
                        LoginPage(onGoogleSignInClick = {
                            firebaseAuthHelper.signIn() // Trigger Google Sign-In
                        })
                    }
                    composable("workoutCreator") { PartOfBodyPage(navController) }
                    composable("userWorkouts") {
                        UserWorkoutsPage(navController, firebaseAuthHelper)
                    }
                    composable(
                        route = "workoutDetails/{workoutJson}",
                        arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                        val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                        WorkoutDetailsPage(workout)
                    }
                    // TODO: Pass down the firebaseAuthHelper variable to the UserSettingsPage.
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
