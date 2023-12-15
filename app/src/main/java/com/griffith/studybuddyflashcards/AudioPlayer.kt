// AudioPlayer.kt
package com.griffith.studybuddyflashcards

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AudioPlayer(
    private val context: Context
) {

    // MediaPlayer instance for audio playback
    private var mediaPlayer: MediaPlayer? = null

    // Function to play an audio file
    fun playFile(file: File) {
        stop() // Stop any ongoing playback

        // Check if the file exists
        if (file.exists()) {
            // Create a MediaPlayer with the provided file URI
            MediaPlayer.create(context, file.toUri()).apply {
                mediaPlayer = this
                start() // Start playback
            }
        } else {
            // Log an error if the file does not exist
            Log.e("AudioPlayer", "File does not exist: $file")
        }
    }

    // Function to stop audio playback
    fun stop() {
        mediaPlayer?.stop() // Stop playback
        mediaPlayer?.release() // Release resources
        mediaPlayer = null // Set MediaPlayer instance to null
    }
}
