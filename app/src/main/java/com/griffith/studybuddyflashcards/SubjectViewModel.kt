package com.griffith.studybuddyflashcards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.griffith.studybuddyflashcards.record.AudioRecorder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class SubjectViewModel(
    application: Application,
    private val daoSubject: SubjectDao,
    private val daoFlashcard: FlashcardDao
): AndroidViewModel(application) {

    private val audioRecorder by lazy {
        AudioRecorder(application)
    }

    private var audioFile: File? = null

    private val audioPlayer: AudioPlayer by lazy {
        AudioPlayer(application)
    }


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
//        Log.d("SubjectViewModel", "Fetching flashcards for subjectId: $subjectId")

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

        _stateFlashcard.value = _stateFlashcard.value.copy(
            subjectId = newSubjectId,
            currentFlashcardIndex = 0
        )
        Log.d("updateFlashcardState", "Updating flashcard index: $newSubjectId with index: ${stateFlashcard.value.currentFlashcardIndex}")
        _stateFlashcard.value = _stateFlashcard.value.copy(
            subjectId = newSubjectId,
            currentFlashcardIndex = 0
        )
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
                    subjectId = event.subjectId,
                    audioFilePath = stateFlashcard.value.audioFilePath
                )

                viewModelScope.launch {
                    daoFlashcard.upsertFlashcard(flashcard)
                }

                _stateFlashcard.update { it.copy(
                    isAddingFlashcard = false,
                    front = "",
                    back = "",
                    subjectId = event.subjectId,
                    audioFilePath = null
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
//                Log.d("ViewModel", "HideFlashcardDialog event received")
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

            AppEvent.NavigateToNextFlashcard -> {
                val flashcards = getFlashcardsBySubjectId(stateFlashcard.value.subjectId)
                _stateFlashcard.update { it.copy(
                    currentFlashcardIndex = (stateFlashcard.value.currentFlashcardIndex + 1) % flashcards.size
                )}
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

            is AppEvent.SaveAudioFile -> {
                _stateFlashcard.update { it.copy(audioFilePath = event.audioFile.path) }
            }



            AppEvent.StartRecordingAudio -> {
                val timestamp = System.currentTimeMillis()
                audioFile = File(getApplication<Application>().cacheDir, "audio_$timestamp.mp3")
                audioRecorder.start(audioFile!!)
                _stateFlashcard.update { it.copy(isRecordingAudio = true) }
                Log.d("VoiceNotes", "Started recording audio: ${audioFile?.path}")

            }

            AppEvent.StopRecordingAudio -> {
                audioRecorder.stop()
                audioFile?.let { savedAudioFile ->
                    // Notify ViewModel or UI about the saved audio file
                    onEvent(AppEvent.SaveAudioFile(savedAudioFile))
                    // Update the state with the saved audio file
                    _stateFlashcard.update {
                        it.copy(
                            isRecordingAudio = false,
                            audioFilePath = savedAudioFile.path
                        )
                    }
                    Log.d("VoiceNotes", "Stopped recording audio: ${savedAudioFile.path}")
                }
            }

            is AppEvent.PlayAudio -> {
                if(event.file == null) {
                    Log.d("VoiceNotes", "File is null")
                    return
                }
                Log.d("VoiceNotes", "Filepath: ${event.file.path}")
                audioPlayer.playFile(event.file)
                _stateFlashcard.update { it.copy(isPlayingAudio = true) }
            }


            is AppEvent.StopAudio -> {
                audioPlayer.stop()
                _stateFlashcard.update { it.copy(isPlayingAudio = false) }
            }
        }
    }
}