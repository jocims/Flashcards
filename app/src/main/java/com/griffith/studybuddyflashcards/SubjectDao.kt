package com.griffith.studybuddyflashcards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Upsert
    suspend fun upsertSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Query("SELECT * FROM subject")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM subject ORDER BY subjectName")
    fun getSubjectsOrderedBySubjectName(): Flow<List<Subject>>
}
