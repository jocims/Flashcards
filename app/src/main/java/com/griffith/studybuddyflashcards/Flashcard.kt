package com.griffith.studybuddyflashcards

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.File

@Entity(
    // Define a foreign key relationship with the Subject table
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,  // Referenced entity class (Subject)
            parentColumns = ["id"],  // Column in the parent entity (Subject)
            childColumns = ["subjectId"],  // Column in the child entity (Flashcard)
            onDelete = ForeignKey.CASCADE  // Specify cascading delete for referential integrity
        )
    ]
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true)  // Primary key with auto-generation
    val id: Int = 0,  // Unique identifier for the flashcard

    val front: String,  // Front side text of the flashcard
    val back: String,  // Back side text of the flashcard

    val subjectId: Int,  // Foreign key referencing the associated Subject

    val audioFilePath: String? = null  // Optional audio file path for associated audio
)



