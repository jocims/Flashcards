// AudioRecorder.kt
package com.griffith.studybuddyflashcards

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class AudioRecorder(
    private val context: Context
) {
    // MediaRecorder instance for audio recording
    private var recorder: MediaRecorder? = null

    // Function to create a new MediaRecorder instance based on API level
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    // Function to start audio recording
    fun start(outputFile: File) {
        // Create a new MediaRecorder instance
        createRecorder().apply {
            // Set audio source, output format, audio encoder, and output file
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            // Prepare and start recording
            prepare()
            start()

            // Set the recorder instance
            recorder = this
        }
    }

    // Function to stop audio recording
    fun stop() {
        // Stop and reset the recorder
        recorder?.stop()
        recorder?.reset()
        recorder = null // Set MediaRecorder instance to null
    }
}