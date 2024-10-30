package com.example.gymsmart.components.pages

import BottomNavigationBar
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

/**
 * Home page (Splash Page)
 *
 * @param navController
 * @param firebaseAuthHelper
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    // Function to handle the user setting navigation
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
        bottomBar = { navController.currentBackStackEntry?.destination?.route?.let {
            BottomNavigationBar(navController, it)
        } },
        modifier = Modifier.fillMaxSize(),
         // Dark background for modern look
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome section
            Text(
                text = "Welcome back, ${UserSession.userName}!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Track your progress and create new workouts.",
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Create Workout Button
            Button(
                onClick = { navController.navigate("workoutCreator") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                //colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Create Workout", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
