package com.example.gymsmart.components.pages
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.gymsmart.R
import com.example.gymsmart.components.ui.BottomNavigationBar
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.firebase.FirebaseAuthHelper
import com.example.gymsmart.firebase.UserSession
import com.example.gymsmart.firebase.WorkoutData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    var mostRecentWorkout by remember { mutableStateOf<WorkoutData?>(null) }
    val quotes = listOf(
        "Push yourself, because no one else is going to do it for you.",
        "The body achieves what the mind believes.",
        "Success starts with self-discipline.",
        "What seems impossible today will become your warm-up tomorrow.",
        "Believe in yourself and all that you are."
    )
    val dailyQuote = remember { quotes[Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % quotes.size] }


    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            db.collection("users").document(userId).collection("workouts")
                .orderBy("dateAdded", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        mostRecentWorkout = documents.first().toObject(WorkoutData::class.java)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("HomePage", "Error getting most recent workout", e)
                }
        }
    }

    fun navigateUserSettingMenu(setting: String) {
        when (setting) {
            "Settings" -> navController.navigate("settings")
            "Logout" -> navController.navigate("logout")
            else -> Log.w("Navigation", "Unknown setting: $setting")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "GymSmart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    UserSettingsDropdownMenu(
                        { setting -> navigateUserSettingMenu(setting) },
                        firebaseAuthHelper = firebaseAuthHelper,
                        navController
                    )
                }
            )
        },
        bottomBar = {
            navController.currentBackStackEntry?.destination?.route?.let {
                BottomNavigationBar(navController, it)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Heading Section
                val greeting = remember {
                    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    when (hour) {
                        in 0..11 -> "Good Morning"
                        in 12..17 -> "Good Afternoon"
                        else -> "Good Evening"
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "$greeting, ${UserSession.userName ?: "Guest"}!",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
                                .format(Calendar.getInstance().time),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Daily Motivational Quote
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dailyQuote,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Stay motivated and crush your goals!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }


                mostRecentWorkout?.let { workout ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Most Recent Workout",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCE93D8), // Lighter purple color
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Name: ${workout.name}",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Muscle Group: ${workout.muscleGroup}",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Date: ${workout.dateAdded.toDate().toString()}",
                                fontSize = 14.sp
                            )
                            Text(
                                text = "💪 Keep pushing forward! ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFCE93D8), // Lighter purple color,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                } ?: Text(
                    text = "No recent workouts found.",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                //  MyCustomDialog()
            }
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
            .height(50.dp)

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
                modifier = Modifier
                    .padding(top = 35.dp)
                    .height(70.dp)
                    .fillMaxWidth()
            )

            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Congratulations!!🥵🥵", color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = "You have earned a badge!", color = Color.Black,

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
                //     TextButton(onClick = { openDialog.value = false }) {
                //          Text(
                //             text = "Not now", fontWeight = FontWeight.Bold, color = Color.White,
                //                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                //           )
                //       }

                TextButton(onClick = { openDialog.value = false }) {
                    Text(
                        text = "Thanks!!", fontWeight = FontWeight.Bold, color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    )
                }

            }


        }


    }

}