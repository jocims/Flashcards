package com.griffith.studybuddyflashcards

import java.io.File

sealed interface AppEvent {

    // SaveSubject event for saving a subject
    object SaveSubject : AppEvent

    // SetSubjectName event for updating the subject name
    data class SetSubjectName(val subjectName: String) : AppEvent

    // ShowSubjectDialog event for displaying the subject dialog
    object ShowSubjectDialog : AppEvent

    // HideSubjectDialog event for hiding the subject dialog
    object HideSubjectDialog : AppEvent

    // SortSubjects event for sorting subjects based on a specified type
    data class SortSubjects(val sortType: SortType) : AppEvent

    // DeleteSubject event for deleting a subject
    data class DeleteSubject(val subject: Subject) : AppEvent

    // SaveFlashcard event for saving a flashcard associated with a subject
    data class SaveFlashcard(val subjectId: Int) : AppEvent

    // SetFlashcardFront event for updating the front of a flashcard
    data class SetFlashcardFront(val front: String) : AppEvent

    // SetFlashcardBack event for updating the back of a flashcard
    data class SetFlashcardBack(val back: String) : AppEvent

    // ShowFlashcardDialog event for displaying the flashcard dialog
    object ShowFlashcardDialog : AppEvent

    // HideFlashcardDialog event for hiding the flashcard dialog
    object HideFlashcardDialog : AppEvent

    // ShowFlashcardList event for displaying the flashcard list
    object ShowFlashcardList : AppEvent

    // SortFlashcards event for sorting flashcards based on a specified type
    data class SortFlashcards(val sortType: SortType) : AppEvent

    // DeleteFlashcard event for deleting a flashcard
    data class DeleteFlashcard(val flashcard: Flashcard) : AppEvent

    // NavigateToPreviousFlashcard event for navigating to the previous flashcard
    object NavigateToPreviousFlashcard : AppEvent

    // NavigateToNextFlashcard event for navigating to the next flashcard
    object NavigateToNextFlashcard : AppEvent

    // StartRecordingAudio event for initiating audio recording
    object StartRecordingAudio : AppEvent

    // StopRecordingAudio event for stopping audio recording
    object StopRecordingAudio : AppEvent

    // SaveAudioFile event for saving the recorded audio file
    data class SaveAudioFile(val audioFile: File) : AppEvent

    // PlayAudio event for playing an audio file
    data class PlayAudio(val file: File) : AppEvent

    // StopAudio event for stopping audio playback
    object StopAudio : AppEvent
}

