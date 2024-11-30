import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymsmart.components.ui.YoutubePlayer
import com.example.gymsmart.firebase.WorkoutData
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Trash
import com.example.gymsmart.BuildConfig
import com.example.gymsmart.api.searchYouTubeVideos
import kotlinx.coroutines.launch
import com.composables.icons.lucide.MoveLeft
import androidx.navigation.NavController
import com.example.gymsmart.components.ui.UserSettingsDropdownMenu
import com.example.gymsmart.firebase.FirebaseAuthHelper
import androidx.compose.material3.TextButton
import com.composables.icons.lucide.Pencil
import com.example.gymsmart.firebase.deleteWorkout
import com.google.firebase.auth.FirebaseAuth


/**
 * Workout details
 *
 * @param workoutData
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsPage(workoutData: WorkoutData?, navController: NavController, firebaseAuthHelper: FirebaseAuthHelper) {
    val apiKey = BuildConfig.GOOGLE_API_KEY
    Log.w("WorkoutDetailsPageAPIKEY", apiKey)

    var showVideoSpinner by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    var videoIds by remember { mutableStateOf<List<String>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) } // State for dialog visibility

    // Fetch video based on exercise/workout name
    LaunchedEffect(workoutData?.name) {
        workoutData?.name?.let { workoutName ->
            coroutineScope.launch {
                val fetchedVideoIds = searchYouTubeVideos(workoutName, apiKey)
                videoIds = fetchedVideoIds
                showVideoSpinner = false
            }
        }
    }

    // Function to handle the user setting navigation
    fun navigateUserSettingMenu(setting: String) {
        when (setting) {
            "Settings" -> navController.navigate("settings")
            "Logout" -> navController.navigate("logout")
            else -> Log.w("Navigation", "Unknown setting: $setting")
        }
    }

    // Display workout details
    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        { setting -> navigateUserSettingMenu(setting) },
                        firebaseAuthHelper = firebaseAuthHelper,
                        navController
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF1c1c1c)),
            )
        }
    ) { innerPadding ->
        workoutData?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = it.name,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Row {
                        IconButton(
                            onClick = {
                                val userId = FirebaseAuth.getInstance().currentUser?.uid
                                workoutData.id.let { workoutId ->
                                    navController.navigate("editWorkout/$userId/$workoutId")
                                } ?: Log.w("WorkoutDetailsPage", "Workout ID is null")
                            }
                        ) {
                            Icon(
                                imageVector = Lucide.Pencil,
                                contentDescription = "Edit Workout",
                                tint = Color.Blue
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Lucide.Trash,
                                contentDescription = "Delete Icon",
                                tint = Color.Red
                            )
                        }

                    }
                }


                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        if (workoutData.isPR) {
                            Text(
                                text = "🎉 New PR!",
                                color = Color(0xFF4CAF50), // Green color for the PR badge
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(text = "PR Details: ${workoutData.prDetails}", fontSize = 16.sp)
                        }
                        Text("Sets: ${it.sets}")
                        Text("Reps: ${it.reps}")
                        Text("Weight: ${it.weight}Lbs")
                        Text("Muscle Group: ${it.muscleGroup}")
                    }
                }

                HorizontalDivider()

                // Display the spinner while loading multiple videos
                if (showVideoSpinner) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                        Text("Loading videos...")
                    }
                } else {
                    if (videoIds.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            videoIds.filter { it.isNotEmpty() }.forEach { videoId ->
                                YoutubePlayer(videoId = videoId)
                            }
                        }
                    } else {
                        Text("No videos found for this exercise!")
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    workoutData?.id?.let { deleteWorkout(it, {navController.popBackStack()}, { Log.w("WorkoutDetailsPage", "Failed to delete ${workoutData.id}")}) }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Workout") },
            text = { Text("Are you sure you want to delete this workout? This action cannot be undone.") }
        )
    }
}