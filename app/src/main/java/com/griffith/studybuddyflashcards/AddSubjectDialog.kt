package com.griffith.studybuddyflashcards

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
fun AddSubjectDialog(
    state: SubjectState,
    onEvent: (AppEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(AppEvent.HideSubjectDialog)
        },
        title = { Text(text = "Add Subject") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.subjectName,
                    onValueChange = {
                        onEvent(AppEvent.SetSubjectName(it))
                    },
                    placeholder = {
                        Text(text = "Subject name")
                    }
                )
//                TextField(
//                    value = state.notes,
//                    onValueChange = {
//                        onEvent(AppEvent.SetNotes(it))
//                    },
//                    placeholder = {
//                        androidx.compose.material.Text(text = "Notes")
//                    }
//                )
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = {
                        onEvent(AppEvent.SaveSubject)
                    }
                ) {
                    Text(text = "Add")
                }
            }
        }
    )
}