package com.example.gymsmart
import HomePage
import WorkoutDetailsPage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymsmart.firebase.FirebaseAuthHelper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gymsmart.components.pages.AttachmentsPage

import com.example.gymsmart.components.pages.LoginPage
import com.example.gymsmart.components.pages.UserSettingsPage
import com.example.gymsmart.components.pages.workouts.UserWorkoutsPage
import com.example.gymsmart.components.pages.workouts.WorkoutCreatorPage
import com.example.gymsmart.components.pages.workouts.WorkoutVideoPage
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

            MyCustomDialog()
            
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
                    composable("workoutCreator") { WorkoutCreatorPage(navController, firebaseAuthHelper) }

                    // User Workouts Page
                    composable("workouts") { UserWorkoutsPage(navController) }

                    // Workout Video Page
                    composable("workoutVideo") {
                        WorkoutVideoPage(
                            navController = navController,
                            firebaseAuthHelper = firebaseAuthHelper
                        )
                    }

                        // Workout Details Page
                    composable(
                        route = "workoutDetails/{workoutJson}",
                        arguments = listOf(navArgument("workoutJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val workoutJson = backStackEntry.arguments?.getString("workoutJson")
                        val workout = Gson().fromJson(workoutJson, WorkoutData::class.java)
                        WorkoutDetailsPage(workoutData = workout, navController = navController, firebaseAuthHelper )
                    }
                    // User Settings Page
                    composable("settings") { UserSettingsPage(navController, firebaseAuthHelper) }


                    composable("attachments") { AttachmentsPage( navController) }
                    
                  
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




@Composable
private fun MyCustomDialog() {
    var openAlert = remember {
        mutableStateOf(false)
    }


    Button(
        onClick = { openAlert.value = true },
        modifier = Modifier
            .width(200.dp)
            .height(1000.dp)

    ) {
        Text(text = "Click me!")
    }

    if (openAlert.value) {
        CustomDialogUI(openAlert)
    }


}

@Composable
private fun CustomDialogUI(openDialogBox: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogBox.value = false }) {
        CustomUI(openDialogBox)
    }


}

@Composable
private fun CustomUI(openDialog: MutableState<Boolean>) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.background(Color.White)
        ) {

            /**Image*/
            Image(
                painter = painterResource(id = R.drawable.gymblck),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    color = Color.Magenta
                ),
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth()
            )

            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Get Updates",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "Allow permisson to send notifications when new update added on play store!",

                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp, 25.dp, 25.dp, 25.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium

                )


            }
            /** Buttons*/
            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Not now", fontWeight = FontWeight.Bold, color = Color.Blue,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Allow", fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

            }


        }


    }

}

