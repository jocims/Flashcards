package com.griffith.studybuddyflashcards

import java.util.Date

data class SubjectState(
    val subjects: List<Subject> = emptyList(),
    val subjectName: String = "",
//    val notes: String = "",
    val isAddingSubject: Boolean = false,
//    val isEditingSubject: Boolean = false,
//    val isDeletingSubject: Boolean = false,
    val sortType: SortType = SortType.SUBJECT_NAME
)
