import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymsmart.components.ui.YoutubePlayer
import com.example.gymsmart.firebase.WorkoutData
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.gymsmart.BuildConfig
import com.example.gymsmart.api.searchYouTubeVideo
import kotlinx.coroutines.launch
import com.composables.icons.lucide.MoveLeft

/**
 * Workout details
 *
 * @param workoutData
 */
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        workoutData?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Workout Name: ${it.name}")
                Text("Muscle Group: ${it.muscleGroup}")
                Text("Sets: ${it.sets}")
                Text("Reps: ${it.reps}")
                Text("Weight: ${it.weight}")

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
