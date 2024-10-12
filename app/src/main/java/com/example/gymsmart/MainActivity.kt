
package com.example.gymsmart
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
            val navController = rememberNavController()
            firebaseAuthHelper = FirebaseAuthHelper(this, navController, signInLauncher)

            NavHost(navController = navController, startDestination = "login") {
                // Existing routes from the GymSmart-MainActivity
                composable("home") {
                    HomePage(navController = navController, onSignOutClick = {
                        firebaseAuthHelper.signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    })
                }
                composable("login") {
                    Login(onGoogleSignInClick = { firebaseAuthHelper.signIn() })
                }
                composable("userWorkouts") {
                    UserWorkouts(navController)
                }
                composable("workoutCreator") {
                    PartOfBodySelector(navController)
                }
                composable("workoutDetails/{workoutJson}", arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })) { backStackEntry ->
                    val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                    val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                    WorkoutDetails(workout)
                }
                composable("muscleGroupSelector/{workoutNames}", arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })) { navBackStackEntry ->
                    val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
                    val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                    MuscleGroupSelector(navController, workoutNames)
                }

                // Additional routes from the anew-MainActivity
                composable("main") { MainPage(navController) }
                composable("workouts/{workoutNames}", arguments = listOf(navArgument("workoutNames") { type = NavType.StringType })) { navBackStackEntry ->
                    val workoutNamesString = navBackStackEntry.arguments?.getString("workoutNames")
                    val workoutNames = workoutNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                    Workouts(navController, workoutNames)
                }
                composable("workout/{workoutName}/{videoIds}", arguments = listOf(navArgument("workoutName") { type = NavType.StringType }, navArgument("videoIds") { type = NavType.StringType })) { navBackStackEntry ->
                    val workoutName = navBackStackEntry.arguments?.getString("workoutName")
                    val videoIdsString = navBackStackEntry.arguments?.getString("videoIds")
                    val videoIds = videoIdsString?.split(",") ?: listOf()
                    if (workoutName != null) {
                        Workout(navController, workoutName, videoIds)
                    }
                }
                composable("videoPlayer/{videoId}", arguments = listOf(navArgument("videoId") { type = NavType.StringType })) { navBackStackEntry ->
                    val videoId = navBackStackEntry.arguments?.getString("videoId")
                    VideoPlayerScreen(videoId)
                }
            }
        }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                firebaseAuthHelper.handleSignInResult(result.data)
            } else {
                Log.w("MainActivity", "Google sign-in canceled or failed")
            }
        }
}

@Composable
fun MainPage(navController: NavController) {
    val upperWorkouts = arrayOf("Chest", "Biceps", "Triceps", "Shoulders", "Lats")
    val lowerWorkouts = arrayOf("Hamstring", "Glutes", "Quadriceps", "Calves")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                onClick = { navController.navigate("workouts/${upperWorkouts.joinToString(",")}") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Go to Upper Body Workouts")
            }
            Button(
                onClick = { navController.navigate("workouts/${lowerWorkouts.joinToString(",")}") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text("Go to Lower Body Workouts")
            }
        }
    }
}

@Composable
fun VideoPlayerScreen(videoId: String?) {
    val context = LocalContext.current

    androidx.compose.runtime.LaunchedEffect(Unit) {
        videoId?.let {
            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                putExtra("VIDEO_ID", videoId)
            }
            context.startActivity(intent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainPage(rememberNavController())
}
