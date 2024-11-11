package com.example.gymsmart.components.pages

import BottomNavigationBar
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.firebase.FirebaseAuthHelper
import com.example.gymsmart.firebase.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
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
                title = { Text("GymSmart", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) },
                actions = {
                    UserSettingsDropdownMenu(
                        { setting -> navigateUserSettingMenu(setting) },
                        firebaseAuthHelper = firebaseAuthHelper,
                        navController
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1c1c1c)),
            )
        },
        bottomBar = {
            navController.currentBackStackEntry?.destination?.route?.let {
                BottomNavigationBar(navController, it)
            }
        },
        floatingActionButton = {
            FloatingActionButtonWithMenu(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Main content in a Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome back, ${UserSession.userName ?: "Guest"}!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Track your progress and create new workouts.",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("workoutCreator") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Create Workout", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun FloatingActionButtonWithMenu(navController: NavController) {
    var isMenuExpanded by remember { mutableStateOf(false) } // State to track if the menu is open

    Column(
        horizontalAlignment = Alignment.End, // Align to the end (right side)
        verticalArrangement = Arrangement.spacedBy(8.dp), // Space between FABs
        modifier = Modifier.padding(16.dp)
    ) {
        if (isMenuExpanded) {
            // Additional Action Button for "Video"
            FloatingActionButton(
                onClick = {navController.navigate("workoutVideo")},
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Video")
            }

            FloatingActionButton(
                onClick = {navController.navigate("workoutCreator") },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Add")
            }
        }

        FloatingActionButton(
            onClick = { isMenuExpanded = !isMenuExpanded }, // Toggle the menu
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}
