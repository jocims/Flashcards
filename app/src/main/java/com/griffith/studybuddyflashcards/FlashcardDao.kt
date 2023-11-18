package com.griffith.studybuddyflashcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Upsert
    suspend fun upsertFlashcard(flashcard: Flashcard)

    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcard WHERE subjectId = :subjectId")
    fun getFlashcardsForSubject(subjectId: Int): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcard WHERE subjectId = :subjectId ORDER BY front")
    fun getFlashcardsOrderedByFront(subjectId: Int): Flow<List<Flashcard>>
}