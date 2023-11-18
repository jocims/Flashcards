package com.griffith.studybuddyflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.griffith.studybuddyflashcards.ui.theme.StudyBuddyFlashcardsTheme

class StudyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val subjectId = intent.getIntExtra("subjectId", -1)

        setContent {
            StudyBuddyFlashcardsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyScreen(subjectId)
                }
            }
        }
    }
}

@Composable
fun StudyScreen(subjectId: Int) {
    // Your StudyActivity UI goes here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Study Activity for Subject ID: $subjectId")
    }
}

@Preview(showBackground = true)
@Composable
fun StudyScreenPreview() {
    StudyBuddyFlashcardsTheme {
        StudyScreen(subjectId = 1)
    }
}







