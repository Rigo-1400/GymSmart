package com.example.gymsmart.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

suspend fun searchYouTubeVideos(exerciseName: String, apiKey: String, maxResults: Int = 2): List<String> {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        // Form the YouTube search URL, with adjustable maxResults to return multiple videos
        val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=$exerciseName%20exercise%20demo&key=$apiKey&maxResults=$maxResults"

        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Parse the JSON response to get the video IDs
            val jsonObject = JSONObject(responseBody)
            val items = jsonObject.getJSONArray("items")

            val videoIds = mutableListOf<String>()
            for (i in 0 until items.length()) {
                val videoId = items.getJSONObject(i).getJSONObject("id").getString("videoId")
                videoIds.add(videoId)
            }

            Log.d("YouTubeAPI", "Found video IDs: $videoIds")
            return@withContext videoIds
        } catch (e: Exception) {
            Log.e("YouTubeAPI", "Error fetching videos: ${e.message}", e)
            return@withContext emptyList<String>()
        }
    }
}
