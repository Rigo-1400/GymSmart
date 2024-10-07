package com.example.gymsmart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

@Composable
fun HomePage(navController: NavController?, onSignOutClick: (() -> Unit)?) {
    Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Hello ${UserSession.userName ?: "NULL"}!")
            AsyncImage(UserSession.userPhotoUrl, "${UserSession.userName} Profile Picture")

            Button(
                onClick = { navController?.navigate("createWorkout") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Create Workout")
            }
            // Workout Details Button
            Button(
                onClick = { navController?.navigate("workoutList") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Workout Details")
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