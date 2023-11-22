package com.griffith.studybuddyflashcards

import java.io.File

data class FlashcardState(
    val flashcards: List<Flashcard> = emptyList(),
    val front: String = "",
    val back: String = "",
    val isAddingFlashcard: Boolean = false,
    val sortType: SortType = SortType.NAME,
    val currentFlashcardIndex: Int = 0,
    val subjectId: Int = -1,
    val isRecordingAudio: Boolean = false, // Add this line
    val isPlayingAudio: Boolean = false, // Add this line
    val audioFilePath: String? = null // Add this property
)


