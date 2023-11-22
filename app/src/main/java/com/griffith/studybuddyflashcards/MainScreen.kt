package com.griffith.studybuddyflashcards

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    state: SubjectState,
    onEvent: (AppEvent) -> Unit,
    viewModel: SubjectViewModel,
    navController: NavController  // Add this parameter

//    context: Context,

) {

    val context = LocalContext.current

    // Collect the flashcards directly from the ViewModel
//    val flashcardsState by viewModel.stateFlashcard.collectAsState()
//    val flashcards = remember {
//        flashcardsState.flashcards
//    }

    Scaffold (
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
        if(state.isAddingSubject) {
            AddSubjectDialog(state = state, onEvent = onEvent)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.values().forEach { sortType ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    onEvent(AppEvent.SortSubjects(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(AppEvent.SortSubjects(sortType))
                                }
                            )
                            Text(text = sortType.name)
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

//                            val dataMap = mapOf(
//                                "subjectId" to subject.id,
//                                "subjectName" to subject.subjectName,
//                            )

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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


}
//
//fun launchStudyActivity(context: Context, id: Int) {
//    val intent = Intent(context, StudyActivity::class.java)
//    intent.putExtra("subjectId", id)
//    context.startActivity(intent)
//}
