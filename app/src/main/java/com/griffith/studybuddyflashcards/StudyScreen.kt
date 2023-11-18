package com.griffith.studybuddyflashcards

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.griffith.studybuddyflashcards.ui.theme.StudyBuddyFlashcardsTheme

class StudyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "your-database-name").build()
        val daoSubject: SubjectDao = appDatabase.subjectDao
        val daoFlashcard: FlashcardDao = appDatabase.flashcardDao()


        //val subjectId = intent.getIntExtra("subjectId", -1)
        val dataMap = intent.getSerializableExtra("dataMap") as? HashMap<String, Any>
        val viewModel: SubjectViewModel = SubjectViewModel(daoSubject, daoFlashcard)

            setContent {
            StudyBuddyFlashcardsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StudyScreen(dataMap, viewModel)
                }
            }
        }
    }
}

@Composable
fun StudyScreen(
    dataMap: HashMap<String, Any>?,
    viewModel: SubjectViewModel
) {
    // Handle the event, state, and view model inside the composable, using local variables or remember
    // Your StudyScreen content here...

    // Example of handling event and state
    var isAddingFlashcard by remember { mutableStateOf(false) }
    var flashcardState by remember { mutableStateOf(FlashcardState()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isAddingFlashcard = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Flashcard"
                )
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    ) { _ ->
        if (isAddingFlashcard) {
//            AddFlashcardDialog(state = state, onEvent = onEvent)
            AddFlashcardDialog(state = flashcardState, onEvent = viewModel::onEvent)
            }


        Column {
            Text("Subject: ${dataMap?.get("subjectName")}")
        }
    }
}









