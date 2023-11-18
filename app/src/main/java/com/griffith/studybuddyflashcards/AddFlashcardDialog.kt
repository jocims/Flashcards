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
                // Add log statement to check the state of the front text
                Log.d("AddFlashcardDialog", "Front Text: ${state.front}")

                TextField(
                    value = state.front,
                    onValueChange = {
                        Log.d("AddFlashcardDialog", "Back Text: $it")

                        onEvent(AppEvent.SetFlashcard(it, state.back))
                    },
                    placeholder = {
                        Text(text = "Flashcard Front Text")
                    }
                )

                TextField(
                    value = state.back,
                    onValueChange = {
                        Log.d("AddFlashcardDialog", "Back Text: $it")

                        onEvent(AppEvent.SetFlashcard(state.front, it))
                    },
                    placeholder = {
                        Text(text = "Flashcard Back Text")
                    }
                )

            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        onEvent(AppEvent.SaveFlashcard)
                    }
                ) {
                    Text(text = "Add")
                }
            }
        }
    )
}
