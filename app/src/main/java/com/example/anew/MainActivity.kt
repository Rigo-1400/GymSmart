package com.example.anew

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anew.components.CreateWorkout
import com.example.anew.components.HomePage
import com.example.anew.components.Login
import com.example.anew.components.Workout
import com.example.anew.components.WorkoutDetails
import com.example.anew.components.WorkoutList
import com.example.anew.components.Workouts
import com.example.anew.firebase.FirebaseAuthHelper
import com.example.anew.firebase.WorkoutData
import com.google.gson.Gson

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val defaultWorkout = WorkoutData(
                id = "default",
                name = "Default Workout",
                muscleGroup = "No Muscle Group",
                partOfTheBody = "Unknown"
            )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuthHelper.handleSignInResult(requestCode, data)
    }
}
