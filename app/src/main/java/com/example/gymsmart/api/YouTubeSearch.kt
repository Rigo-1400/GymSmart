package com.example.gymsmart.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

suspend fun searchYouTubeVideo(exerciseName: String, apiKey: String): String? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        // Form the YouTube search URL
        val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=$exerciseName%20exercise%20demo&key=$apiKey&maxResults=1"

        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            // Parse the JSON response to get the video ID
            val jsonObject = JSONObject(responseBody)
            val items = jsonObject.getJSONArray("items")
            if (items.length() > 0) {
                val videoId = items.getJSONObject(0).getJSONObject("id").getString("videoId")
                Log.d("YouTubeAPI", "Found video ID: $videoId")
                return@withContext videoId
            } else {
                Log.w("YouTubeAPI", "No video found for $exerciseName")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e("YouTubeAPI", "Error fetching video: ${e.message}", e)
            return@withContext null
        }
    }
}
