import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Trash
import com.example.gymsmart.BuildConfig
import com.example.gymsmart.api.searchYouTubeVideo
import kotlinx.coroutines.launch

/**
 * Workout details
 *
 * @param workoutData
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailsPage(workoutData: WorkoutData?) {
    val apiKey = BuildConfig.GOOGLE_API_KEY
    Log.w("WorkoutDetailsPageAPIKEY", apiKey)
    var videoId by remember { mutableStateOf<String?>(null) }
    var showVideoSpinner by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch video based on exercise/workout name
    LaunchedEffect(workoutData?.name) {
        workoutData?.name?.let { workoutName ->
            coroutineScope.launch {
                val fetchedVideoId = searchYouTubeVideo(workoutName, apiKey)
                videoId = fetchedVideoId
                showVideoSpinner = false
            }
        }
    }

    // Display workout details
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "GymSmart",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
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
                        modifier = Modifier.weight(1f), // Pushes the icons to the end of the row
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Row {
                        IconButton(onClick = { /* Handle edit */ }) {
                            Icon(imageVector = Lucide.Pencil, contentDescription = "Edit Icon")
                        }
                        IconButton(onClick = { /* Handle delete */ }) {
                            Icon(imageVector = Lucide.Trash, contentDescription = "Delete Icon", tint = Color.Red)
                        }
                    }
                }
                Card(Modifier.fillMaxWidth()) {
                    Text("Sets: ${it.sets}")
                    Text("Reps: ${it.reps}")
                    Text("Weight: ${it.weight}")
                    Text("Muscle Group: ${it.muscleGroup}")
                }

                HorizontalDivider()

                // Display the spinner while we look for the exercise/workout video
                if(showVideoSpinner) { CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally)); Text("Loading video...") }
                if (videoId != null && !showVideoSpinner) {
                    YoutubePlayer(videoId = videoId!!)
                } else if(videoId == null && !showVideoSpinner) {
                    Text("No video found for this exercise!")
                }
            }
        }
    }
}