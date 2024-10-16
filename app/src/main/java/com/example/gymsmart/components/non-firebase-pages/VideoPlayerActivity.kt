package com.example.gymsmart

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoPlayerActivity : AppCompatActivity() {
    private var selectedVideoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set full-screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_android_youtube_player)

        // Hide the action bar
        supportActionBar?.hide()

        // Get the YouTubePlayerView from the layout
        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.videoPlayer)

        // Add the YouTubePlayerView as a lifecycle observer
        lifecycle.addObserver(youTubePlayerView)

        // Retrieve the video ID from intent
        selectedVideoId = intent.getStringExtra("VIDEO_ID")

        if (selectedVideoId == null) {
            // Show a toast message if no video ID is found
            Toast.makeText(this, "Video ID not found!", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
            return
        }

        // Set up YouTubePlayer listener
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // Load the video only if a valid video ID is present
                selectedVideoId?.let { videoId ->
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                // Optionally handle other player states
                super.onStateChange(youTubePlayer, state)
            }
        })
    }
}
