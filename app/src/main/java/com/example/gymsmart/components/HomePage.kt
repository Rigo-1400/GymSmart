package com.example.gymsmart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gymsmart.firebase.UserSession

/**
 * Home page
 *
 * @param navController
 * @param onSignOutClick
 */
@Composable
fun HomePage(navController: NavController?, onSignOutClick: (() -> Unit)?) {
    val upperWorkouts = arrayOf("Chest", "Biceps", "Triceps", "Shoulders", "Lats")
    val lowerWorkouts = arrayOf("Hamstring", "Glutes", "Quadriceps", "Calves")
    val attList = arrayOf("Cable", "Rope", "V-Bar", "Straight Bar")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hello ${UserSession.userName ?: "NULL"}!")
            AsyncImage(UserSession.userPhotoUrl, "${UserSession.userName} Profile Picture")

            Button(
                onClick = { navController?.navigate("workoutCreator") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Create Workout")
            }
            // Workout Details Button
            Button(
                onClick = { navController?.navigate("userWorkouts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Workout Details")
            }
            
            Button(
                onClick = {navController?.navigate("attatchements/${attList.joinToString (",")}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Additional Accessories")
            }










            // Sign-out button
            if (onSignOutClick != null) {
                Button(
                    onClick = onSignOutClick, // Call the sign-out function
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Sign Out")
                }
            }

        }
    }
}