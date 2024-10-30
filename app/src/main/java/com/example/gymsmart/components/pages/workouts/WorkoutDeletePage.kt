package com.example.gymsmart.components.pages.workouts

import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.launch

@Composable
fun WorkoutDeletePage(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid
    var workouts by remember { mutableStateOf<List<WorkoutData>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Fetch workouts from Firestore
    LaunchedEffect(userId) {
        if (userId != null) {
            db.collection("workouts")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { result ->
                    val fetchedWorkouts = result.map { document ->
                        WorkoutData(
                            id = document.id,
                            name = document.getString("name") ?: "",
                            sets = document.getLong("sets")?.toInt() ?: 0,
                            reps = document.getLong("reps")?.toInt() ?: 0,
                            weight = document.getLong("weight")?.toInt() ?: 0,
                            muscleGroup = document.getString("muscleGroup") ?: "",
                            partOfBody = document.getString("partOfBody") ?: ""
                        )
                    }
                    workouts = fetchedWorkouts
                    loading = false
                }
        }
    }

    // Function to delete workout from Firestore
    fun deleteWorkoutFromFirebase(workoutId: String) {
        if (userId != null) {
            db.collection("workouts").document(workoutId).delete().addOnSuccessListener {
                    Log.d("Firebase", "Workout successfully deleted!")
                    workouts = workouts.filter { it.id != workoutId } // Update the list after deletion
                }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error deleting workout", e)
                }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Deleting Workout",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (loading) {
                CircularProgressIndicator()
            } else if (workouts.isEmpty()) {
                Text("No workouts available.")
            } else {
                // Display the list of workouts with delete buttons
                workouts.forEach { workout ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = workout.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Sets: ${workout.sets}, Reps: ${workout.reps}, Weight: ${workout.weight} lbs",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Button(
                                onClick = {
                                    deleteWorkoutFromFirebase(workout.id)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete", color = Color.White)
                            }
                        }
                    }
                }
            }

            // Back to Workouts Button
            Button(
                onClick = { navController.navigate("userWorkouts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Back to Workouts",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Data class for WorkoutData
data class WorkoutData(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Int,
    val muscleGroup: String,
    val partOfBody: String
)


