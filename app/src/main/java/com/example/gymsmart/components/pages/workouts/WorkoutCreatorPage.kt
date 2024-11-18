package com.example.gymsmart.components.pages.workouts

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Minus
import com.composables.icons.lucide.MoveLeft
import com.composables.icons.lucide.Plus
import com.example.gymsmart.components.ui.MuscleGroupSelectorDropdownMenu
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.firebase.FirebaseAuthHelper
import com.example.gymsmart.firebase.checkForPR
import com.example.gymsmart.firebase.saveWorkoutToFirebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCreatorPage(navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var workoutName by remember { mutableStateOf("") }
    var muscleGroup by remember { mutableStateOf("") }
    var partOfTheBody by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf(1) }
    var reps by remember { mutableStateOf(1) }
    var weight by remember { mutableStateOf(10) }

    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val userId = firebaseAuth.currentUser?.uid

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("GymSmart", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Lucide.MoveLeft, contentDescription = "Move Back to Previous Page")
                    }
                },
                actions = {
                    UserSettingsDropdownMenu(
                        { setting -> if (setting == "Settings") navController.navigate("settings") },
                        firebaseAuthHelper = firebaseAuthHelper,
                        navController
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1c1c1c)),
            )
        },
        content = { innerPadding ->
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

                            // When the user is about to save the workout
                            coroutineScope.launch {
                                val (isPR, _, prDetails) = checkForPR(workoutName, weight, reps, userId)

                                if (isPR) {
                                    Toast.makeText(context, "🎉 New PR! You hit a personal record for $workoutName!", Toast.LENGTH_SHORT).show()
                                }

                                saveWorkoutToFirebase(db, userId, Timestamp.now(), partOfTheBody, workoutName, muscleGroup, sets, reps, weight, isPR, prDetails)
                                navController.navigate("workouts")
                            }
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
    )
}

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
        modifier = Modifier.size(40.dp)
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
        modifier = Modifier.size(40.dp)
    ) {
        Icon(Lucide.Plus, contentDescription = "Increase $label", tint = Color.White)
    }
}

