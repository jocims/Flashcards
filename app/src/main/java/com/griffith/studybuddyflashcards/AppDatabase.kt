package com.griffith.studybuddyflashcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Subject::class, Flashcard::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract val subjectDao: SubjectDao

    abstract fun flashcardDao(): FlashcardDao

}
