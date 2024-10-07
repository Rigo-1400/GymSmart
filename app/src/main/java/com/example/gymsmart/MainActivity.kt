package com.example.gymsmart

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymsmart.components.CreateWorkout
import com.example.gymsmart.components.HomePage
import com.example.gymsmart.components.Login
import com.example.gymsmart.components.Workout
import com.example.gymsmart.components.WorkoutDetails
import com.example.gymsmart.components.WorkoutList
import com.example.gymsmart.components.Workouts
import com.example.gymsmart.firebase.FirebaseAuthHelper
import com.example.gymsmart.firebase.WorkoutData
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create NavController in the setContent block
            val navController = rememberNavController()

            // Initialize FirebaseAuthHelper with the navController
            firebaseAuthHelper = FirebaseAuthHelper(this, navController)


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
                    route = "workouts/{workoutNames}",
                    arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
                    val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                    Workouts(navController, workoutNames)
                }

                composable(
                    route = "workout/{partOfTheBody}/{muscleGroup}",
                    arguments = listOf(navArgument("partOfTheBody") { type = NavType.StringType }, navArgument("muscleGroup") { NavType.StringType })
                ) { navBackStackEntry ->
                    val pOfTheBody = navBackStackEntry.arguments?.getString("partOfTheBody")
                    val mGroup = navBackStackEntry.arguments?.getString("muscleGroup")
                    if (pOfTheBody != null && mGroup != null) {
                        Workout(navController, pOfTheBody, mGroup)
                    }
                }
                composable("login") {
                    // Login Screen
                    Login(onGoogleSignInClick = {
                        firebaseAuthHelper.signIn() // Trigger Google Sign-In
                    })
                }
                composable("createWorkout") { CreateWorkout(navController) }
                composable("workoutList") {
                    WorkoutList(navController)
                }
                composable(
                    route = "workoutDetail/{workoutJson}",
                    arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                    val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                    WorkoutDetails(workout)
                }


            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuthHelper.handleSignInResult(requestCode, data)
    }
}
