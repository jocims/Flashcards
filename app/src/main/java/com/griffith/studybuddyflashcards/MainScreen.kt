package com.griffith.studybuddyflashcards

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.toList
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: SubjectState,
    onEvent: (AppEvent) -> Unit,
    viewModel: SubjectViewModel,
    navController: NavController  // Add this parameter
) {

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { },
                actions = {

                    Image(
                        painter = painterResource(id = R.drawable.header),  // Replace with your image resource
                        contentDescription = "Header Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                },
            )
        },
        floatingActionButton = {
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
                painter = painterResource(id = R.drawable.background2), // Replace with your image resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
                    .statusBarsPadding()
            )


            if (state.isAddingSubject) {
                AddSubjectDialog(state = state, onEvent = onEvent)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(16.dp), // Adjust padding as needed
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center // Center the content horizontally
                    ) {
                        SortType.values().forEach { sortType ->
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onEvent(AppEvent.SortSubjects(sortType))
                                    }
                                    .fillMaxWidth(), // Ensure the inner Row takes the full width
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.sortType == sortType,
                                    onClick = {
                                        onEvent(AppEvent.SortSubjects(sortType))
                                    },
                                    modifier = Modifier.padding(end = 8.dp) // Adjust padding as needed
                                )

                                Text(
                                    text = sortType.name,
                                    modifier = Modifier.padding(end = 50.dp) // Adjust padding as needed
                                )

                            }
                        }
                    }

                }
                items(state.subjects) { subject ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showToast(context, "Clicked on ${subject.subjectName}")

                                //Set the currentFlashcardIndex to 0
                                viewModel.updateFlashcardState(subject.id)

                                // Navigate to StudyScreen
                                navController.navigate("study_screen/${subject.id}")
                            },

                        ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = subject.subjectName,
                                fontSize = 20.sp
                            )
                        }
                        IconButton(onClick = {
                            onEvent(AppEvent.DeleteSubject(subject))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete subject"
                            )
                        }
                    }
                }
            }
        }
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
