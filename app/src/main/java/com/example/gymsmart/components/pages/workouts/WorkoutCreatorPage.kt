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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.Plus
import com.example.gymsmart.components.ui.MuscleGroupSelectorDropdownMenu
import com.example.gymsmart.firebase.saveWorkoutToFirebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun WorkoutCreatorPage(navController: NavController) {
    var workoutName by remember { mutableStateOf("") }
    var muscleGroup by remember { mutableStateOf("") }
    var partOfTheBody by remember { mutableStateOf("") }
    var sets by remember { mutableIntStateOf(1) }
    var reps by remember { mutableIntStateOf(1) }
    var weight by remember { mutableIntStateOf(10) }

    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

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
                text = "Create Your Workout",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            MuscleGroupSelectorDropdownMenu { muscleGroupSelected, muscleGroupPartOfBody ->
                muscleGroup = muscleGroupSelected
                partOfTheBody = muscleGroupPartOfBody
            }

            TextField(
                value = workoutName,
                onValueChange = { workoutName = it },
                label = { Text("Workout Name") },
                placeholder = { Text("e.g., Bench Press") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            @Composable
            fun RowScope.CounterSection(
                label: String,
                value: Int,
                onDecrement: () -> Unit,
                onIncrement: () -> Unit,
                unit: String? = null
            ) {
                Text(
                    text = "$label:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDecrement,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(Lucide.Minus, contentDescription = "Decrease $label", tint = Color.White)
                }
                Text(
                    "$value${unit ?: ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(
                    onClick = onIncrement,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    Icon(Lucide.Plus, contentDescription = "Increase $label", tint = Color.White)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) { CounterSection("Sets", sets, { if (sets > 1) sets-- }, { sets++ }) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) { CounterSection("Reps", reps, { if (reps > 1) reps-- }, { reps++ }) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) { CounterSection("Weight", weight, { if (weight > 0) weight -= 5 }, { weight += 5 }, " lbs") }

            Button(
                onClick = {
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
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Save Workout",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
