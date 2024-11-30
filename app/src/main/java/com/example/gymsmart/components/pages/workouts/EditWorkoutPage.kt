package com.example.gymsmart.components.pages.workouts

import android.util.Log
import androidx.compose.foundation.layout.*
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
import com.composables.icons.lucide.MoveLeft
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.firebase.WorkoutData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.gymsmart.firebase.FirebaseAuthHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutPage(navController: NavController, userId: String, workoutId: String?, firebaseAuthHelper: FirebaseAuthHelper) {
    var workoutDataState by remember { mutableStateOf<WorkoutData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var dialogMessage by remember { mutableStateOf("") }

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

    var name by remember { mutableStateOf(workoutData.name) }
    var muscleGroup by remember { mutableStateOf(workoutData.muscleGroup) }
    var sets by remember { mutableStateOf(workoutData.sets.toString()) }
    var reps by remember { mutableStateOf(workoutData.reps.toString()) }
    var weight by remember { mutableStateOf(workoutData.weight.toString()) }
    var isSaving by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("GymSmart", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Lucide.MoveLeft, contentDescription = "Move Back Previous Page")
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
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Workout Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            TextField(
                value = muscleGroup,
                onValueChange = { muscleGroup = it },
                label = { Text("Muscle Group") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            TextField(
                value = sets,
                onValueChange = { sets = it },
                label = { Text("Sets") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            TextField(
                value = reps,
                onValueChange = { reps = it },
                label = { Text("Reps") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val setsInt = sets.toIntOrNull()
                    val repsInt = reps.toIntOrNull()
                    val weightInt = weight.toIntOrNull()

                    if (setsInt == null || repsInt == null || weightInt == null) {
                        snackbarMessage = "Invalid numbers for Sets, Reps, or Weight."
                    } else {
                        isSaving = true
                        coroutineScope.launch {
                            Log.d("EditWorkoutPage", "Updating workout: $workoutId")
                            updateWorkout(
                                userId = userId,
                                workoutId = workoutId ?: "",
                                name = name,
                                muscleGroup = muscleGroup,
                                sets = setsInt,
                                reps = repsInt,
                                weight = weightInt,
                                onSuccess = {
                                    isSaving = false
                                    dialogMessage = "Workout updated successfully!"
                                    navController.navigate("workouts")
                                },
                                onFailure = { e ->
                                    isSaving = false
                                    snackbarMessage = "Error updating workout: ${e.message}"
                                }
                            )
                        }
                    }
                },
                enabled = !isSaving
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


// Fetch workout data function
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

// Update workout data function
fun updateWorkout(
    userId: String,
    workoutId: String,
    name: String,
    muscleGroup: String,
    sets: Int,
    reps: Int,
    weight: Int,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val documentRef = firestore.collection("users")
        .document(userId)
        .collection("workouts")
        .document(workoutId)

    val updatedData = mapOf(
        "name" to name,
        "muscleGroup" to muscleGroup,
        "sets" to sets,
        "reps" to reps,
        "weight" to weight
    )

    documentRef.update(updatedData)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
}

fun navigateUserSettingMenu(setting: String, navController: NavController) {
    when (setting) {
        "Settings" -> navController.navigate("settings")
        "Logout" -> navController.navigate("logout")
        else -> Log.w("Navigation", "Unknown setting: $setting")
    }
}
