package com.griffith.studybuddyflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.studybuddyflashcards.ui.theme.StudyBuddyFlashcardsTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "subjects.db"
        ).build()
    }

    private val viewModel: SubjectViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubjectViewModel(db.subjectDao, db.flashcardDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyBuddyFlashcardsTheme {
                // Create a NavController
                val navController = rememberNavController()

                // Set up NavHost with the defined navigation graph
                NavHost(
                    navController = navController,
                    startDestination = "main_screen"
                ) {
                    // Define the screens
                    composable("main_screen") {
                        val state by viewModel.stateSubject.collectAsState()
                        MainScreen(state = state, onEvent = viewModel::onEvent, viewModel = viewModel, navController = navController)
                    }

                    composable("study_screen/{subjectId}") { backStackEntry ->
                        // Retrieve subjectId from the route
                        val subjectId = backStackEntry.arguments?.getString("subjectId")?.toIntOrNull()
                        val flashcards = viewModel.stateFlashcard.collectAsState().value.flashcards
                        if (subjectId != null) {
                            StudyScreen(
                                subjectId = subjectId,
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        } else {
                            // Handle invalid subjectId
                        }
                    }

                }
            }
        }
    }
}

