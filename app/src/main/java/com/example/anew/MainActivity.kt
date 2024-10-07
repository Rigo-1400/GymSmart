package com.example.anew

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anew.components.HomePage
import com.example.anew.components.Login
import com.example.anew.components.Workout
import com.example.anew.components.Workouts
import java.util.Calendar

import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.ui.tooling.preview.Preview
import com.example.anew.components.Attatchements
import com.example.anew.components.Attatchements2
import java.util.Date
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Create NavController in the setContent block
            showDatePicker(this)
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
    ////////////

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuthHelper.handleSignInResult(requestCode, data)
    }
}

@Composable
fun showDatePicker(context: Context)
{
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    month = calendar.get(Calendar.MONTH)

    calendar.time = Date()

    val date = remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,

        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/$month/ $year"
        }, year, month, day
    )

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Open Calendar: ${date.value}")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {datePickerDialog.show()}) {
            Text(text = "Open")
        }
    }


}
