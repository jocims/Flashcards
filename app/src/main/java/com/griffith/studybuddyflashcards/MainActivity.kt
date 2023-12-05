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


const val REQUEST_CODE_PERMISSION = 123 // You can use any integer value
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
                return SubjectViewModel(application, db.subjectDao, db.flashcardDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // Request audio recording permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSION
        )


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
                        MainScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            viewModel = viewModel,
                            navController = navController)
                    }

                    composable("study_screen/{subjectId}") { backStackEntry ->
                        // Retrieve subjectId from the route
                        val subjectId = backStackEntry.arguments?.getString("subjectId")?.toIntOrNull()
                        if (subjectId != null) {
                            StudyScreen(
                                subjectId = subjectId,
                                viewModel = viewModel,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }

                    composable("quiz_screen/{subjectId}") { backStackEntry ->
                        // Retrieve subjectId from the route
                        val subjectId = backStackEntry.arguments?.getString("subjectId")?.toIntOrNull()
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

