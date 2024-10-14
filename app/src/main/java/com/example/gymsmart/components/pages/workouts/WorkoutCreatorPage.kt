package com.example.gymsmart.components.pages.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymsmart.firebase.saveWorkoutToFirebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Workout
 *
 * @param navController
 * @param partOfTheBody
 * @param muscleGroup
 */
@Composable
fun WorkoutCreatorPage(navController: NavController, partOfTheBody: String, muscleGroup: String) {
    // State variables to track user input
    var workoutName by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(1) }
    var reps by remember { mutableIntStateOf(1) }
    var weight by remember { mutableIntStateOf(10) }

    // Firestore and Auth instances
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Your $muscleGroup Workout",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Input for Workout Name
            TextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                label = { Text("Workout Name") },
                placeholder = { Text("e.g., Bench Press") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            // Input for Sets using +/- buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Sets:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { if (sets > 1) sets-- },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("-", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("$sets", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { sets++ },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Input for Reps using +/- buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Reps:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { if (reps > 1) reps-- },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("-", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("$reps", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { reps++ },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            // Input for Reps using +/- buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Weight:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { if (weight > 1) weight -= 5 },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("-", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Text("${weight}lbs", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { weight += 5 },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Gray)
                ) {
                    Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Save Button
            Button(
                onClick = {
                    // Check if user is logged in and input is not empty before saving
                    if (userId != null && sets > 0 && reps > 0) {
                        saveWorkoutToFirebase(
                            db, userId, Timestamp.now(),
                            partOfTheBody, workoutName, muscleGroup, sets, reps, weight
                        )
                        navController.navigate("userWorkouts")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Save Workout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
