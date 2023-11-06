package com.griffith.studybuddyflashcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Subject::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val subjectDao: SubjectDao
}
