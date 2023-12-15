package com.griffith.studybuddyflashcards

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: SubjectState,
    onEvent: (AppEvent) -> Unit,
    viewModel: SubjectViewModel,
    navController: NavController  // Add this parameter
) {
    val context = LocalContext.current

    // Scaffold is used to create a Material Design scaffolded screen
    Scaffold (
        topBar = {
            // TopAppBar is used for displaying the top app bar
            TopAppBar(
                title = { },
                actions = {
                    // Displaying an Image in the top app bar
                    Image(
                        painter = painterResource(id = R.drawable.header),
                        contentDescription = "Header Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                },
            )
        },
        floatingActionButton = {
            // FloatingActionButton is used for the add subject action
            FloatingActionButton(onClick = {
                onEvent(AppEvent.ShowSubjectDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add subject"
                )
            }
        },
        modifier = Modifier.padding(bottom = 16.dp)
    ) { _ ->
        // Box is used as a container for other composables
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            // Image is used for displaying a background image
            Image(
                painter = painterResource(id = R.drawable.background2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .statusBarsPadding()
            )

            // Checking if the subject dialog is open for adding a new subject
            if (state.isAddingSubject) {
                // AddSubjectDialog is displayed when adding a new subject
                AddSubjectDialog(state = state, onEvent = onEvent)
            }

            // LazyColumn is used for displaying a list of subjects
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Row for displaying sorting options
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SortType.values().forEach { sortType ->
                            // Row for each sorting option
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onEvent(AppEvent.SortSubjects(sortType))
                                    }
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // RadioButton for selecting the sorting option
                                RadioButton(
                                    selected = state.sortType == sortType,
                                    onClick = {
                                        onEvent(AppEvent.SortSubjects(sortType))
                                    },
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                // Text displaying the sorting option
                                Text(
                                    text = sortType.name,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(end = 50.dp),
                                )
                            }
                        }
                    }
                }

                // Displaying subjects in rows with two subjects per row
                items(state.subjects.chunked(2)) { subjectsRow ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        subjectsRow.forEach { subject ->
                            // Box for each subject
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clickable {
                                        showToast(context, "Clicked on ${subject.subjectName}")

                                        // Set the currentFlashcardIndex to 0
                                        viewModel.updateFlashcardState(subject.id)

                                        // Navigate to StudyScreen
                                        navController.navigate("study_screen/${subject.id}")
                                    }
                            ) {
                                // Box with an Image for displaying the subject
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Transparent)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.subject),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                // Column for displaying the subject details
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Text displaying the subject name
                                    Text(
                                        text = subject.subjectName,
                                        fontSize = 30.sp,
                                        style = MaterialTheme.typography.body2,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Function for displaying a Toast message
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

