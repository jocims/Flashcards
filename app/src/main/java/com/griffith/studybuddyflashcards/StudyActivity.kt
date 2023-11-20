package com.griffith.studybuddyflashcards

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        val viewModel = SubjectViewModel(daoSubject, daoFlashcard)


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
//    var flashcardState by remember { mutableStateOf(FlashcardState()) }

    val flashcardState by viewModel.stateFlashcard.collectAsState()


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

            // Add log statements to check the values
            Log.d("StudyScreen", "Adding Flashcard - subjectId: ${dataMap?.get("subjectId") as? Int ?: -1}")
            Log.d("StudyScreen", "FlashcardState: $flashcardState")


            AddFlashcardDialog(state = flashcardState, subjectId = dataMap?.get("subjectId") as? Int ?: -1, onEvent = viewModel::onEvent)
            }

        Column {
            Text("Subject: ${dataMap?.get("subjectName")}")
        }
    }
}









