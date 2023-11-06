package com.griffith.studybuddyflashcards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class SubjectViewModel(
    private val daoSubject: SubjectDao
//    private val daoFlashcard: FlashcardDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.DEFAULT)
    private val _subjects = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.SUBJECT_NAME -> daoSubject.getSubjectsOrderedBySubjectName()
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

//    private val _stateFlashcard = MutableStateFlow(FlashcardState())


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

//            is AppEvent.SetNotes -> {
//                _stateSubject.update { it.copy(
//                    notes = event.notes
//                ) }
            is AppEvent.DeleteSubject -> TODO()
            AppEvent.HideSubjectDialog -> TODO()
            AppEvent.SaveSubject -> TODO()
            is AppEvent.SetSubjectName -> TODO()
            AppEvent.ShowSubjectDialog -> TODO()
            is AppEvent.SortSubjects -> TODO()
        }
        }
    }

//}