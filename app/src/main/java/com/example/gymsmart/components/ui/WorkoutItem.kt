package com.example.gymsmart.components.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gymsmart.firebase.WorkoutData
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Workout item
 *
 * @param workout
 * @param navController
 */
@Composable
fun WorkoutItem(workout: WorkoutData, navController: NavController) {
    val gson = Gson()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                val workoutJson = gson.toJson(workout)
                navController.navigate("workoutDetails/$workoutJson")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val formatter = SimpleDateFormat("MM/dd/yy hh:mm a", Locale.getDefault())
        val formattedDate = formatter.format(workout.dateAdded.toDate())
        Column(modifier = Modifier.padding(16.dp)) {
            Text(workout.name, style = MaterialTheme.typography.titleMedium)
            Text("Muscle Group: ${workout.muscleGroup}", style = MaterialTheme.typography.bodyMedium)
            Text(formattedDate, style = MaterialTheme.typography.bodyMedium)
            if (workout.isPR) {
                Text(
                    text = "🎉 Personal Record!",
                    color = Color(0xFF4CAF50), // Green color for PR message
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
