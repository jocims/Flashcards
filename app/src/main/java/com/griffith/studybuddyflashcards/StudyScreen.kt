package com.griffith.studybuddyflashcards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun StudyScreen(
    state: FlashcardState,
    subjectId: Int,
    viewModel: SubjectViewModel,
    navController: NavController,
    onEvent: (AppEvent) -> Unit
    ) {
    val subjectDetails = viewModel.getSubjectDetails(subjectId ?: -1)

    // Example of handling event and state
//    var isAddingFlashcard by remember { mutableStateOf(false) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AppEvent.ShowFlashcardDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Flashcard"
                )
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    ) { _ ->
        if (state.isAddingFlashcard) {
            // Pass subjectId to AddFlashcardDialog
            AddFlashcardDialog(subjectId = subjectId ?: -1, state = state, onEvent = viewModel::onEvent)
        }

        Column {
            // Display subject details
            Text("Subject ID: $subjectId")
            Text("Subject Name: ${subjectDetails?.subjectName ?: "Unknown"}")

            // Additional UI components...
        }
    }
}