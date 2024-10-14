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
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.gymsmart.components.Attatchements
import com.example.gymsmart.components.Attatchements2
import com.example.gymsmart.components.MuscleGroupSelector
import com.example.gymsmart.components.PartOfBodySelector
import com.example.gymsmart.firebase.WorkoutData
import com.google.gson.Gson
import java.lang.reflect.Modifier
import java.util.Calendar
import java.util.Date
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold




class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create NavController in the setContent block
            val navController = rememberNavController()
            showDatePicker(context = this)
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


                composable(
                    route = "attatchements/{attatchementsNames}",
                    arguments = listOf(navArgument("attatchementsNames") { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val attatchementsNamesString = navBackStackEntry.arguments?.getString("attatchementsNames")
                    val attatchementsNames = attatchementsNamesString?.split(",")?.toTypedArray() ?: arrayOf()
                    Attatchements(navController, attatchementsNames)
                }


                composable(
                    route = "attatchements2/{attatchementsNames}",
                    arguments = listOf(navArgument("attatchementsNames") {type = NavType.StringType })
                ) { navBackStackEntry ->
                    val name = navBackStackEntry.arguments?.getString("attatchementsNames")
                    name?.let{Attatchements2(name)}


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




@Composable
fun showDatePicker(context: Context) {
    val year: Int
    val month: Int
    val day: Int
    var selectedDate by remember { mutableStateOf<Long?>(null) }


    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    month = calendar.get(Calendar.MONTH)


    calendar.time = Date()

    val date = remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,

        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "Workouts for selected date:" + "$dayOfMonth/$month/ $year"
        }, year, month, day
    )


    val wrkout = println("Workouts for today: ")
    Column(

        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally )
    {
        Text(text = "Open Calendar: ${date.value}",)

        Button(onClick = { datePickerDialog.show() }) {
            Text(text = "Open")
        }

    }




}
