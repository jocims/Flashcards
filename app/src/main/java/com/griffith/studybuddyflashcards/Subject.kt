package com.griffith.studybuddyflashcards

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectName: String,
//    val notes: String
)

