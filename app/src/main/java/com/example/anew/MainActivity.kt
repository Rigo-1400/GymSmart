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
import com.example.anew.Components.HomePage
import com.example.anew.Components.Login
import com.example.anew.Components.Workout
import com.example.anew.Components.Workouts

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
                    route = "workout/{workoutName}",
                    arguments = listOf(navArgument("workoutName") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val name = navBackStackEntry.arguments?.getString("workoutName")
                    name?.let { Workout(name) }
                }
                composable("login") {
                    // Login Screen
                    Login(onGoogleSignInClick = {
                        firebaseAuthHelper.signIn() // Trigger Google Sign-In
                    })
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuthHelper.handleSignInResult(requestCode, data)
    }
}
