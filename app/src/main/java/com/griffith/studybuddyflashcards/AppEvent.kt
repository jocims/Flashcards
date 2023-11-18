package com.griffith.studybuddyflashcards

sealed interface AppEvent {

    object SaveSubject : AppEvent
    data class SetSubjectName(val subjectName: String) : AppEvent
    object ShowSubjectDialog : AppEvent
    object HideSubjectDialog : AppEvent
    data class SortSubjects(val sortType: SortType) : AppEvent
    data class DeleteSubject(val subject: Subject) : AppEvent
//    data class SetNotes(val notes: String) : AppEvent
    object SaveFlashcard : AppEvent
    data class SetFlashcard(val front: String, val back: String) : AppEvent
    object ShowFlashcardDialog : AppEvent
    object HideFlashcardDialog : AppEvent
    object ShowFlashcardList : AppEvent

    data class SortFlashcards(val sortType: SortType) : AppEvent

    data class DeleteFlashcard(val flashcard: Flashcard) : AppEvent

}