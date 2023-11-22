package com.griffith.studybuddyflashcards

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun AddFlashcardDialog(
    state: FlashcardState,
    onEvent: (AppEvent) -> Unit,
    subjectId: Int,
    modifier: Modifier = Modifier
) {
    // Add log statement to check if the composable is recomposed
    Log.d("AddFlashcardDialog", "Recomposing with state: $state")

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AppEvent.HideFlashcardDialog)
        },
        title = { Text(text = "Add Flashcard") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Add log statements to check the values
                Log.d("AddFlashcardDialog", "Recomposing with state: $state")
                Log.d("AddFlashcardDialog", "SubjectId: $subjectId")

                TextField(
                    value = state.front,
                    onValueChange = {
                        // Add log statement to check the changes in the front text
                        Log.d("AddFlashcardDialog", "Front Text: $it")
                        onEvent(AppEvent.SetFlashcardFront(it))
                    },
                    placeholder = {
                        Text(text = "Flashcard Front Text")
                    }
                )

                TextField(
                    value = state.back,
                    onValueChange = {
                        // Add log statement to check the changes in the back text
                        Log.d("AddFlashcardDialog", "Back Text: $it")
                        onEvent(AppEvent.SetFlashcardBack(it))
                    },
                    placeholder = {
                        Text(text = "Flashcard Back Text")
                    }
                )

                // Button for starting/stopping audio recording
                Button(
                    onClick = {
                        if (state.isRecordingAudio) {
                            // Stop recording
                            onEvent(AppEvent.StopRecordingAudio)
                        } else {
                            // Start recording
                            onEvent(AppEvent.StartRecordingAudio)
                        }
                    }
                ) {
                    Text(text = if (state.isRecordingAudio) "Stop" else "Record Back Audio")
                }

            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        Log.d("AddFlashcardDialog", "Button clicked")

                        // Save the flashcard
                        onEvent(AppEvent.SaveFlashcard(subjectId = subjectId))

                        // Hide the dialog
                        onEvent(AppEvent.HideFlashcardDialog)
                    }
                ) {
                    Text(text = "Add")
                }
            }
        }
    )
}