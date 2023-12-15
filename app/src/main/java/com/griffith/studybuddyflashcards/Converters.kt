package com.griffith.studybuddyflashcards

import androidx.room.TypeConverter
import java.io.File

class Converters {
    // TypeConverter function to convert a File to its string representation
    @TypeConverter
    fun fromFile(file: File?): String? {
        // Return the path of the File, or null if the File is null
        return file?.path
    }

    // TypeConverter function to convert a string representation to a File
    @TypeConverter
    fun toFile(path: String?): File? {
        // Return a File created from the provided path, or null if the path is null
        return path?.let { File(it) }
    }
}

