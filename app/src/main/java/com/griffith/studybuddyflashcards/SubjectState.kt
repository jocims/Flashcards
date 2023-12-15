package com.griffith.studybuddyflashcards

// Data class representing the state related to subjects in the application
data class SubjectState(
    // List of subjects currently available in the application
    val subjects: List<Subject> = emptyList(),

    // The name of the subject (used when adding a new subject)
    val subjectName: String = "",

    // Flag indicating whether the user is currently in the process of adding a new subject
    val isAddingSubject: Boolean = false,

    // The sorting type for subjects (default is SortType.NAME)
    val sortType: SortType = SortType.NAME,
)