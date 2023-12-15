package com.griffith.studybuddyflashcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    // Upsert operation: Insert a new subject or update an existing one
    @Upsert
    suspend fun upsertSubject(subject: Subject)

    // Delete operation: Remove a subject from the database
    @Delete
    suspend fun deleteSubject(subject: Subject)

    // Query to get all subjects from the database
    @Query("SELECT * FROM subject")
    fun getAllSubjects(): Flow<List<Subject>>

    // Query to get all subjects from the database, ordered by subjectName
    @Query("SELECT * FROM subject ORDER BY subjectName")
    fun getSubjectsOrderedBySubjectName(): Flow<List<Subject>>

    // Query to get a specific subject by its ID
    @Query("SELECT * FROM subject WHERE id = :subjectId")
    fun getSubjectById(subjectId: Int): Subject?
}

