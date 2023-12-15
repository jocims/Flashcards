package com.griffith.studybuddyflashcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// Entity class representing a Subject in the database
@Entity
data class Subject(
    // Primary key with auto-generated values
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // The name of the subject
    val subjectName: String,
)


