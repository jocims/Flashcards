package com.griffith.studybuddyflashcards

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubjectViewModel(
    private val daoSubject: SubjectDao,
    private val daoFlashcard: FlashcardDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.DEFAULT)
    private val _subjects = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> daoSubject.getSubjectsOrderedBySubjectName()
//                SortType.NOTES -> daoSubject.getSubjectsOrderedByNotes()
                SortType.DEFAULT -> daoSubject.getAllSubjects()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _stateSubject = MutableStateFlow(SubjectState())
    val stateSubject = combine(_stateSubject, _sortType, _subjects) { stateSubject, sortType, subjects ->
        stateSubject.copy(
            subjects = subjects,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SubjectState())

    private val _flashcards = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> daoFlashcard.getFlashcardsOrderedByFront(stateFlashcard.value.subjectId)
                SortType.DEFAULT -> daoFlashcard.getAllFlashcards()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _stateFlashcard = MutableStateFlow(FlashcardState())
    val stateFlashcard: StateFlow<FlashcardState> = combine(_stateFlashcard, _sortType, _flashcards) { stateFlashcard, sortType,  flashcards ->
        stateFlashcard.copy(
            flashcards = flashcards,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FlashcardState())

    fun getFlashcardsBySubjectId(subjectId: Int): List<Flashcard> {
        // Retrieve flashcards by subjectId from your data source
        Log.d("SubjectViewModel", "Fetching flashcards for subjectId: $subjectId")

        // If the subjectId is -1 or not found, return an empty list
        if (subjectId == -1) {
            return emptyList()
        }

        return stateFlashcard.value.flashcards.filter { it.subjectId == subjectId }
    }

    fun getSubjectDetails(subjectId: Int): Subject? {
        // You may need to modify the logic based on your database structure
        return stateSubject.value.subjects.find { it.id == subjectId }
    }

    fun updateFlashcardState(newSubjectId: Int) {
        _stateFlashcard.value = _stateFlashcard.value.copy(subjectId = newSubjectId)
    }

    fun onEvent(event: AppEvent) {
        when (event) {
            is AppEvent.DeleteSubject -> {
                viewModelScope.launch {
                    daoSubject.deleteSubject(event.subject)
                }
            }
            AppEvent.HideSubjectDialog -> {
                _stateSubject.update { it.copy(
                    isAddingSubject = false
                ) }
            }
            AppEvent.SaveSubject -> {
                val subjectName = stateSubject.value.subjectName
//                val notes = stateSubject.value.notes

//                if(subjectName.isBlank() || notes.isBlank()) {
//                    return
//                }

                if(subjectName.isBlank()){
                    return
                }

                val subject = Subject(
                    subjectName = subjectName
//                    notes = notes
                )
                viewModelScope.launch {
                    daoSubject.upsertSubject(subject)
                }
                _stateSubject.update { it.copy(
                    isAddingSubject = false,
                    subjectName = ""
//                    notes = ""
                ) }
            }
            is AppEvent.SetSubjectName -> {
                _stateSubject.update { it.copy(
                    subjectName = event.subjectName
                ) }
            }
            AppEvent.ShowSubjectDialog -> {
                _stateSubject.update { it.copy(
                    isAddingSubject = true
                ) }
            }
            is AppEvent.SortSubjects -> {
                _sortType.value = event.sortType
            }

            is AppEvent.DeleteFlashcard -> {
                viewModelScope.launch {
                    daoFlashcard.deleteFlashcard(event.flashcard)
                }
            }
            is AppEvent.SaveFlashcard -> {
                val front = stateFlashcard.value.front
                val back = stateFlashcard.value.back

                if(front.isBlank() || back.isBlank()) {
                    return
                }

                val flashcard = Flashcard(
                    front = front,
                    back = back,
                    subjectId = event.subjectId
                )

                viewModelScope.launch {
                    daoFlashcard.upsertFlashcard(flashcard)
                }

                _stateFlashcard.update { it.copy(
                    isAddingFlashcard = false,
                    front = "",
                    back = "",
                    subjectId = -1
                ) }
            }
            is AppEvent.SetFlashcardBack -> {
                _stateFlashcard.update { it.copy(
                    back = event.back
                ) }
            }
            is AppEvent.SetFlashcardFront -> {
                _stateFlashcard.update { it.copy(
                    front = event.front
                ) }
            }
            AppEvent.ShowFlashcardList -> {
                _stateFlashcard.update { it.copy(
                    isAddingFlashcard = false
                ) }
            }
            AppEvent.HideFlashcardDialog -> {
                Log.d("ViewModel", "HideFlashcardDialog event received")
                _stateFlashcard.update { it.copy(
                    isAddingFlashcard = false
                ) }
            }
            AppEvent.ShowFlashcardDialog -> {
                _stateFlashcard.update { it.copy(
                    isAddingFlashcard = true
                ) }
            }

            is AppEvent.SortFlashcards -> {
                _sortType.value = event.sortType
            }
            is AppEvent.DeleteSubject -> TODO()
            AppEvent.SaveSubject -> TODO()
            AppEvent.ShowSubjectDialog -> TODO()
            is AppEvent.SortSubjects -> TODO()

//            AppEvent.NavigateToNextFlashcard -> {
//                _stateFlashcard.update {
//                    it.copy(
//                        currentFlashcardIndex = (stateFlashcard.value.currentFlashcardIndex + 1) % getFlashcardsBySubjectId(subjectId = it.subjectId).size
//                    )
//                }
//            }
//            AppEvent.NavigateToPreviousFlashcard -> {
//                _stateFlashcard.update { it.copy(
//                    currentFlashcardIndex = (stateFlashcard.value.currentFlashcardIndex - 1 + getFlashcardsBySubjectId(subjectId = it.subjectId).size) % getFlashcardsBySubjectId(subjectId = it.subjectId).size
//                ) }
//            }

            AppEvent.NavigateToNextFlashcard -> {
                val flashcards = getFlashcardsBySubjectId(stateFlashcard.value.subjectId)
                if (flashcards.isNotEmpty()) {
                    _stateFlashcard.update { state ->
                        val currentFlashcardIndex = (state.currentFlashcardIndex + 1) % flashcards.size
                        state.copy(currentFlashcardIndex = currentFlashcardIndex)
                    }
                }
            }
            AppEvent.NavigateToPreviousFlashcard -> {
                val flashcards = getFlashcardsBySubjectId(stateFlashcard.value.subjectId)
                if (flashcards.isNotEmpty()) {
                    _stateFlashcard.update { state ->
                        val currentFlashcardIndex = (state.currentFlashcardIndex - 1 + flashcards.size) % flashcards.size
                        state.copy(currentFlashcardIndex = currentFlashcardIndex)
                    }
                }
            }
        }
    }
}