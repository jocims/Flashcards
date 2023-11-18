package com.griffith.studybuddyflashcards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StudyScreen(subjectId: SubjectState, viewModel: SubjectViewModel) {
    val subject by remember { viewModel.getSubjectById(subjectId) }.collectAsState()

    if (subject != null) {
        // Render your StudyScreen with subject data
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Study Activity for Subject ID: ${subject.id}")
            // Add more UI elements using subject data as needed
        }
    } else {
        // Handle the case where the subject is not found
    }
}




