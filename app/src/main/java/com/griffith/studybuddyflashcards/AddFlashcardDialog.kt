package com.griffith.studybuddyflashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Composable function for the "Add Flashcard" dialog
@Composable
fun AddFlashcardDialog(
    state: FlashcardState,  // Represents the state of the flashcard being added
    onEvent: (AppEvent) -> Unit,  // Function to handle events triggered within the dialog
    subjectId: Int,  // Identifier for the subject the flashcard belongs to
    modifier: Modifier = Modifier  // Additional modifiers for customization
) {

    // AlertDialog component that represents the "Add Flashcard" dialog
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AppEvent.HideFlashcardDialog)  // Handle dismiss event by hiding the dialog
        },
        title = { Text(text = "Add Flashcard") },  // Dialog title

        // Dialog content, including text fields for front and back of the flashcard
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Text field for the front of the flashcard
                TextField(
                    value = state.front,
                    onValueChange = {
                        onEvent(AppEvent.SetFlashcardFront(it))
                    },
                    placeholder = {
                        Text(text = "Flashcard Front Text")
                    }
                )

                // Text field for the back of the flashcard
                TextField(
                    value = state.back,
                    onValueChange = {
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
                            // Stop recording audio
                            onEvent(AppEvent.StopRecordingAudio)
                        } else {
                            // Start recording audio
                            onEvent(AppEvent.StartRecordingAudio)
                        }
                    }
                ) {
                    Text(text = if (state.isRecordingAudio) "Stop" else "Record Back Audio")
                }
            }
        },

        // Dialog buttons, including a button to add the flashcard
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Button to add the flashcard
                Button(
                    onClick = {

                        if (state.isRecordingAudio) {
                            // Stop recording audio if it is in progress
                            onEvent(AppEvent.StopRecordingAudio)
                        }

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
