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
import com.example.gymsmart.components.workouts.WorkoutCreator
import com.example.gymsmart.components.HomePage
import com.example.gymsmart.components.Login
import com.example.gymsmart.components.workouts.WorkoutDetails
import com.example.gymsmart.components.workouts.UserWorkouts
import FirebaseAuthHelper
import com.example.gymsmart.components.MuscleGroupSelector
import com.example.gymsmart.components.PartOfBodySelector
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
            firebaseAuthHelper = FirebaseAuthHelper(this, navController, signInLauncher)


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
                    route = "muscleGroupSelector/{workoutNames}",
                    arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
                    val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                    MuscleGroupSelector(navController, workoutNames)
                }

                composable(
                    route = "workoutCreator/{partOfTheBody}/{muscleGroup}",
                    arguments = listOf(navArgument("partOfTheBody") { type = NavType.StringType }, navArgument("muscleGroup") { NavType.StringType })
                ) { navBackStackEntry ->
                    val pOfTheBody = navBackStackEntry.arguments?.getString("partOfTheBody")
                    val mGroup = navBackStackEntry.arguments?.getString("muscleGroup")
                    if (pOfTheBody != null && mGroup != null) {
                        WorkoutCreator(navController, pOfTheBody, mGroup)
                    }
                }
                composable("login") {
                    // Login Screen
                    Login(onGoogleSignInClick = {
                        firebaseAuthHelper.signIn() // Trigger Google Sign-In
                    })
                }
                composable("workoutCreator") { PartOfBodySelector(navController) }
                composable("userWorkouts") {
                    UserWorkouts(navController)
                }
                composable(
                    route = "workoutDetails/{workoutJson}",
                    arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                    val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                    WorkoutDetails(workout)
                }


            }
        }
    }

    // In your Activity, define the launcher using the new Activity Result API
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the result using the FirebaseAuthHelper
                firebaseAuthHelper.handleSignInResult(result.data)
            } else {
                Log.w("MainActivity", "Google sign-in canceled or failed")
            }
        }
}
