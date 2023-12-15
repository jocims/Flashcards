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

// Composable function for the "Add Subject" dialog
@Composable
fun AddSubjectDialog(
    state: SubjectState,  // Represents the state of the subject being added
    onEvent: (AppEvent) -> Unit,  // Function to handle events triggered within the dialog
    modifier: Modifier = Modifier  // Additional modifiers for customization
) {

    // AlertDialog component that represents the "Add Subject" dialog
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AppEvent.HideSubjectDialog)  // Handle dismiss event by hiding the dialog
        },
        title = { Text(text = "Add Subject") },  // Dialog title

        // Dialog content, including a text field for the subject name
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Text field for the subject name
                TextField(
                    value = state.subjectName,
                    onValueChange = {
                        onEvent(AppEvent.SetSubjectName(it))
                    },
                    placeholder = {
                        Text(text = "Subject name")
                    }
                )
            }
        },

        // Dialog buttons, including a button to add the subject
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Button to add the subject
                Button(
                    onClick = {
                        onEvent(AppEvent.SaveSubject)  // Trigger event to save the subject
                    }
                ) {
                    Text(text = "Add")
                }
            }
        }
    )
}
