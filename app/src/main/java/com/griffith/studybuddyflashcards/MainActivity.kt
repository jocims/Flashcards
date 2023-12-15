package com.griffith.studybuddyflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.studybuddyflashcards.ui.theme.StudyBuddyFlashcardsTheme
import android.Manifest


// Define a constant value for requesting permissions
const val REQUEST_CODE_PERMISSION = 123 // You can use any integer value

class MainActivity : ComponentActivity() {

    // Lazy initialization for the Room database
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "subjects.db"
        ).build()
    }

    // Initialize the ViewModel using the lazy delegate and a custom ViewModelProvider.Factory
    private val viewModel: SubjectViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SubjectViewModel(application, db.subjectDao, db.flashcardDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request audio recording and storage permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSION
        )

        // Set the content of the activity using Jetpack Compose
        setContent {
            StudyBuddyFlashcardsTheme {
                // Create a NavController for navigation between screens
                val navController = rememberNavController()

                // Set up NavHost with the defined navigation graph
                NavHost(
                    navController = navController,
                    startDestination = "main_screen"
                ) {
                    // Define the main screen
                    composable("main_screen") {
                        // Collect the state from the ViewModel as a Compose state
                        val state by viewModel.stateSubject.collectAsState()

                        // Display the main screen using the collected state
                        MainScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    // Define the study screen with a dynamic subjectId parameter
                    composable("study_screen/{subjectId}") { backStackEntry ->
                        // Retrieve subjectId from the route
                        val subjectId = backStackEntry.arguments?.getString("subjectId")?.toIntOrNull()

                        // Check if subjectId is valid and display the StudyScreen
                        if (subjectId != null) {
                            StudyScreen(
                                subjectId = subjectId,
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }

                    // Define the quiz screen with a dynamic subjectId parameter
                    composable("quiz_screen/{subjectId}") { backStackEntry ->
                        // Retrieve subjectId from the route
                        val subjectId = backStackEntry.arguments?.getString("subjectId")?.toIntOrNull()

                        // Check if subjectId is valid and display the QuizScreen
                        if (subjectId != null) {
                            QuizScreen(
                                subjectId = subjectId,
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                }
            }
        }
    }
}