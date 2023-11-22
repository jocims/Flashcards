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

    private var mediaPlayer: MediaPlayer? = null

    fun playFile(file: File) {
        stop() // Stop any ongoing playback

        if (file.exists()) {
            MediaPlayer.create(context, file.toUri()).apply {
                mediaPlayer = this
                start()
            }
        } else {
            Log.e("AudioPlayer", "File does not exist: $file")
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

