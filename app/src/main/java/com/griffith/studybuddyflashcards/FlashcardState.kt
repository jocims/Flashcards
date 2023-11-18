package com.griffith.studybuddyflashcards

data class FlashcardState(
    val flashcards: List<Flashcard> = emptyList(),
    val front: String = "",
    val back: String = "",
    val isAddingFlashcard: Boolean = false,
    val sortType: SortType = SortType.NAME
)
