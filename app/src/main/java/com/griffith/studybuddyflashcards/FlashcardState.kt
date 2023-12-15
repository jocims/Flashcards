package com.griffith.studybuddyflashcards

data class FlashcardState(
    // List of flashcards associated with the current subject
    val flashcards: List<Flashcard> = emptyList(),

    // Front text of the flashcard being added or edited
    val front: String = "",

    // Back text of the flashcard being added or edited
    val back: String = "",

    // Flag indicating whether the user is in the process of adding a new flashcard
    val isAddingFlashcard: Boolean = false,

    // Sorting type for the flashcards (default is by name)
    val sortType: SortType = SortType.NAME,

    // Index of the currently displayed flashcard in the list
    val currentFlashcardIndex: Int = 0,

    // ID of the subject associated with the flashcards
    val subjectId: Int = -1,

    // Flag indicating whether audio recording is in progress
    val isRecordingAudio: Boolean = false,

    // Flag indicating whether audio playback is in progress
    val isPlayingAudio: Boolean = false,

    // File path of the recorded audio associated with the flashcard
    val audioFilePath: String? = null
)



