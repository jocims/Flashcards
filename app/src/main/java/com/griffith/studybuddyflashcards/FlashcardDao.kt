package com.griffith.studybuddyflashcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    // Upsert (insert or update) a flashcard in the database
    @Upsert
    suspend fun upsertFlashcard(flashcard: Flashcard)

    // Delete a flashcard from the database
    @Delete
    suspend fun deleteFlashcard(flashcard: Flashcard)

    // Retrieve all flashcards from the database
    @Query("SELECT * FROM flashcard")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    // Retrieve flashcards for a specific subject from the database
    @Query("SELECT * FROM flashcard WHERE subjectId = :subjectId")
    fun getFlashcardsForSubject(subjectId: Int): Flow<List<Flashcard>>

    // Retrieve flashcards for a specific subject ordered by front text from the database
    @Query("SELECT * FROM flashcard WHERE subjectId = :subjectId ORDER BY front")
    fun getFlashcardsOrderedByFront(subjectId: Int): Flow<List<Flashcard>>
}
