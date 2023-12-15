package com.griffith.studybuddyflashcards

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Database annotation with entities and version information
@Database(entities = [Subject::class, Flashcard::class], version = 3)
@TypeConverters(Converters::class) // Add this line to specify TypeConverters for custom types
abstract class AppDatabase : RoomDatabase() {

    // Abstract property representing the SubjectDao
    abstract val subjectDao: SubjectDao

    // Abstract function to obtain the FlashcardDao
    abstract fun flashcardDao(): FlashcardDao

    // Companion object for static members and methods
    companion object {
        // Database name constant
        private const val DATABASE_NAME = "app_database"

        // Volatile variable to ensure thread-safety for double-checked locking
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Function to get an instance of the AppDatabase using Room
        fun getDatabase(context: Context): AppDatabase {
            // If INSTANCE is not null, return it; otherwise, create a new instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Allow destructive migration for simplicity
                    .build()

                // Set INSTANCE to the newly created instance and return it
                INSTANCE = instance
                instance
            }
        }
    }
}