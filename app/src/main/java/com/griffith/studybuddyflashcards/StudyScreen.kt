// StudyScreen.kt
package com.griffith.studybuddyflashcards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    subjectId: Int,
    viewModel: SubjectViewModel,
    navController: NavController,
    onEvent: (AppEvent) -> Unit
) {
    // Call updateFlashcardState when StudyScreen is created
    LaunchedEffect(Unit) {
        viewModel.updateFlashcardState(subjectId)
        viewModel.getFlashcardsBySubjectId(subjectId)
    }

    val context = LocalContext.current

    val state by viewModel.stateFlashcard.collectAsState()
    val subjectDetails = viewModel.getSubjectDetails(subjectId)

    // Fetch the flashcards whenever the state changes
    val flashcardList = remember(subjectId, state.flashcards) {
        viewModel.getFlashcardsBySubjectId(subjectId)
    }

    Scaffold(
        topBar = {
            // Top app bar with navigation and custom content
            TopAppBar(
                title = { },
                actions = {
                    // Spacer and back button in app bar
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close Study Screen"
                        )
                    }
                    // Custom content (image) in app bar
                    Image(
                        painter = painterResource(id = R.drawable.header),
                        contentDescription = "Header Image",
                        modifier = Modifier.fillMaxSize()
                    )
                },
            )
        },
        floatingActionButton = {
            // Floating action button to add flashcards
            FloatingActionButton(onClick = {
                onEvent(AppEvent.ShowFlashcardDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Flashcard"
                )
            }
        },
        modifier = Modifier.background(Color.Transparent),
    ) { _ ->
        // Use a Box to layer the content and the background image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Set a default background color
            // Content of the Box
            contentAlignment = Alignment.Center,
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .statusBarsPadding()
            )

            if (state.isAddingFlashcard) {
                // Show AddFlashcardDialog if adding a flashcard
                AddFlashcardDialog(
                    subjectId = subjectId ?: -1,
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display subject name and flashcards
                item {
                    if (subjectDetails != null) {
                        Text(
                            "${subjectDetails.subjectName} Flashcards",
                            fontSize = 25.sp,
                            fontStyle = FontStyle(1),
                        )
                    }
                }
                // Display flashcards or a message if none found
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (state.flashcards.isNotEmpty()) {
                            // Display flashcards
                            val currentFlashcardIndex = state.currentFlashcardIndex
                            val audioFile =
                                flashcardList.getOrNull(currentFlashcardIndex)?.audioFilePath
                            Flashcard(
                                flashcards = flashcardList,
                                currentFlashcardIndex = currentFlashcardIndex,
                                state = state,
                                onNavigateToPrevious = { onEvent(AppEvent.NavigateToPreviousFlashcard) },
                                onNavigateToNext = { onEvent(AppEvent.NavigateToNextFlashcard) },
                                audioFilePath = audioFile,
                                onPlayAudio = { file ->
                                    val playAudioEvent = AppEvent.PlayAudio(file = file)
                                    viewModel.onEvent(playAudioEvent)
                                },
                                onStopAudio = { viewModel.onEvent(AppEvent.StopAudio) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                onEvent = viewModel::onEvent
                            )
                        } else {
                            // Display message if no flashcards found
                            Text(
                                text = "No flashcards found for this Subject. Click on the '+' sign and create one now!",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Display quiz button
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(0.6f)
                            .height(IntrinsicSize.Max)
                            .clickable {
                                if (flashcardList.size < 2) {
                                    showToast(context, "Not enough flashcards to start quiz")
                                    return@clickable
                                }
                                navController.navigate("quiz_screen/${subjectId}")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.subject),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .size(200.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Text(
                            text = "Quiz",
                            modifier = Modifier
                                .padding(16.dp),
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Flashcard(
    flashcards: List<Flashcard>,
    currentFlashcardIndex: Int,
    state: FlashcardState,
    onNavigateToPrevious: () -> Unit,
    onNavigateToNext: () -> Unit,
    audioFilePath: String?,
    onPlayAudio: (File) -> Unit,
    onStopAudio: () -> Unit,
    modifier: Modifier = Modifier,
    onEvent: (AppEvent) -> Unit
) {
    // State to determine if the front or back of the flashcard is visible
    var isFrontVisible by remember { mutableStateOf(true) }

    // Use LaunchedEffect to automatically stop audio when currentFlashcardIndex changes
    LaunchedEffect(currentFlashcardIndex) {
        onStopAudio()
    }

    // Column composable to organize UI elements vertically
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Box composable containing the flashcard content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isFrontVisible = !isFrontVisible },
            contentAlignment = Alignment.Center
        ) {
            // Image displaying the front or back of the flashcard based on isFrontVisible
            Image(
                painter = painterResource(id = if (isFrontVisible) R.drawable.front else R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .size(230.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            // Previous button
            IconButton(
                onClick = {
                    onNavigateToPrevious()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate to Previous Flashcard"
                )
            }

            // Next button
            IconButton(
                onClick = {
                    onNavigateToNext()
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Navigate to Next Flashcard"
                )
            }

            // Text displaying the front or back content of the flashcard
            Text(
                text = if (isFrontVisible) flashcards.getOrNull(currentFlashcardIndex)?.front.orEmpty() else flashcards.getOrNull(currentFlashcardIndex)?.back.orEmpty(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp),
                color = Color.DarkGray
            )

            // Box at the bottom for audio playback controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Display audio controls when the back of the flashcard is visible and audioFilePath is not null
                if (!isFrontVisible && audioFilePath != null) {
                    // Button for playing/stopping audio
                    IconButton(onClick = {
                        if (state.isPlayingAudio) onStopAudio() else onPlayAudio(File(audioFilePath ?: ""))
                    }) {
                        Icon(
                            imageVector = if (state.isPlayingAudio) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = if (state.isPlayingAudio) "Stop Audio" else "Play Audio"
                        )
                    }
                }
            }
        }
    }
}
