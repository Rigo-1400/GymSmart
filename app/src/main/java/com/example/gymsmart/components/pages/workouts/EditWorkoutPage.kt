package com.example.gymsmart.components.pages.workouts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.gymsmart.firebase.WorkoutData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.gymsmart.firebase.FirebaseAuthHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutPage(
    navController: NavController,
    userId: String,
    workoutId: String?,
    firebaseAuthHelper: FirebaseAuthHelper
) {
    var workoutDataState by remember { mutableStateOf<WorkoutData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isSaving by remember { mutableStateOf(false) }
    var hasUnsavedChanges by remember { mutableStateOf(false) }


    LaunchedEffect(workoutId) {
        if (workoutId != null) {
            try {
                val workoutData = fetchWorkoutFromFirestore(userId, workoutId)
                workoutDataState = workoutData
            } catch (e: Exception) {
                Log.e("EditWorkoutPage", "Error fetching workout data: ${e.message}")
                workoutDataState = null
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val workoutData = workoutDataState
    if (workoutData == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Error: Unable to load workout data.")
        }
        return
    }

    // Editable fields
    var name by remember { mutableStateOf(workoutData.name) }
    var muscleGroup by remember { mutableStateOf(workoutData.muscleGroup) }
    var sets by remember { mutableStateOf(workoutData.sets) }
    var reps by remember { mutableStateOf(workoutData.reps) }
    var weight by remember { mutableStateOf(workoutData.weight) }
    var nameError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Workout", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Lucide.MoveLeft, contentDescription = "Move Back")
                    }
                },
                actions = {
                    UserSettingsDropdownMenu(
                        { setting -> navigateUserSettingMenu(setting, navController) },
                        firebaseAuthHelper = firebaseAuthHelper,
                        navController
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1c1c1c)),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Muscle Group Selector
            MuscleGroupSelectorDropdownMenu { muscleGroupSelected, _ ->
                muscleGroup = muscleGroupSelected
                hasUnsavedChanges = true
            }
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = it.isBlank()
                    hasUnsavedChanges = true
                },
                label = { Text("Workout Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                enabled = !isSaving,
                isError = nameError
            )
            if (nameError) {
                Text(
                    text = "Workout name cannot be empty",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            CounterSection(
                label = "Sets",
                value = sets,
                onDecrement = { if (sets > 1) sets--; hasUnsavedChanges = true },
                onIncrement = { sets++; hasUnsavedChanges = true },
                onValueChanged = { newValue -> sets = newValue; hasUnsavedChanges = true }
            )
            CounterSection(
                label = "Reps",
                value = reps,
                onDecrement = { if (reps > 1) reps--; hasUnsavedChanges = true },
                onIncrement = { reps++; hasUnsavedChanges = true },
                onValueChanged = { newValue -> reps = newValue; hasUnsavedChanges = true }
            )
            CounterSection(
                label = "Weight",
                value = weight,
                onDecrement = { if (weight > 0) weight -= 5; hasUnsavedChanges = true },
                onIncrement = { weight += 5; hasUnsavedChanges = true },
                onValueChanged = { newValue -> weight = newValue; hasUnsavedChanges = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // To save the workout.
            Button(
                onClick = {
                    if (name.isBlank()) {
                        snackbarMessage = "Workout name cannot be empty."
                        return@Button
                    }

                    isSaving = true
                    coroutineScope.launch {
                        try {
                            val firestore = FirebaseFirestore.getInstance()
                            val documentRef = firestore.collection("users")
                                .document(userId)
                                .collection("workouts")
                                .document(workoutId ?: "")
                            val updatedData = mapOf(
                                "name" to name,
                                "muscleGroup" to muscleGroup,
                                "sets" to sets,
                                "reps" to reps,
                                "weight" to weight
                            )
                            documentRef.update(updatedData).await()
                            isSaving = false
                            hasUnsavedChanges = false
                            snackbarMessage = "Workout updated successfully!"
                            navController.navigate("workouts")
                        } catch (e: Exception) {
                            isSaving = false
                            snackbarMessage = "Error updating workout: ${e.message}"
                        }
                    }
                },
                enabled = !isSaving && hasUnsavedChanges
            ) {
                Text("Save Changes")
            }
        }

        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let {
                snackbarHostState.showSnackbar(it)
                snackbarMessage = null
            }
        }
    }
}

// Fetch workout data
suspend fun fetchWorkoutFromFirestore(userId: String, workoutId: String): WorkoutData {
    val firestore = FirebaseFirestore.getInstance()
    val workoutDocument = firestore.collection("users")
        .document(userId)
        .collection("workouts")
        .document(workoutId)
        .get()
        .await()

    return workoutDocument.toObject(WorkoutData::class.java)
        ?: throw Exception("Workout data is null")
}

// Navigate to user settings
fun navigateUserSettingMenu(setting: String, navController: NavController) {
    when (setting) {
        "Settings" -> navController.navigate("settings")
        "Logout" -> navController.navigate("logout")
        else -> Log.w("Navigation", "Unknown setting: $setting")
    }
}

// Counter Section function
@Composable
fun CounterSection(
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    onValueChanged: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                .height(56.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF2C2C2C),
                unfocusedContainerColor = Color(0xFF2C2C2C),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
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
}
