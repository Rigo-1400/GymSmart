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
import androidx.compose.ui.text.style.TextAlign
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
    var sets by remember { mutableIntStateOf(1) }
    var reps by remember { mutableIntStateOf(1) }
    var weight by remember { mutableIntStateOf(10) }

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
                ) {
                    CounterSection(
                        label = "Sets",
                        value = sets,
                        onDecrement = { if (sets > 1) sets-- },
                        onIncrement = { sets++ },
                        onValueChanged = { sets = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CounterSection(
                        label = "Reps",
                        value = reps,
                        onDecrement = { if (reps > 1) reps-- },
                        onIncrement = { reps++ },
                        onValueChanged = { reps = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CounterSection(
                        label = "Weight",
                        value = weight,
                        onDecrement = { if (weight > 0) weight -= 5 },
                        onIncrement = { weight += 5 },
                        onValueChanged = { weight = it },
                    )
                }

                Button(
                    onClick = {
                        if (userId != null && sets > 0 && reps > 0) {
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
    onValueChanged: (Int) -> Unit
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
    TextField(
        value = value.toString(),
        onValueChange = {
            val newValue = it.toIntOrNull()
            if (newValue != null && newValue >= 0) {
                onValueChanged(newValue)
            }
        },
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .height(56.dp), // Increased height for the text field
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 20.sp, // Larger font size
            textAlign = TextAlign.Center // Center the text inside the text field
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF2C2C2C), // Darker background color when focused
            unfocusedContainerColor = Color(0xFF2C2C2C), // Darker background color when not focused
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Focused border color
            unfocusedIndicatorColor = Color.Transparent, // Removes border when not focused
            cursorColor = MaterialTheme.colorScheme.primary // Cursor color
        )
    )
    IconButton(
        onClick = onIncrement,
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.size(40.dp)
    ) {
        Icon(Lucide.Plus, contentDescription = "Increase $label", tint = Color.White)
    }
}

