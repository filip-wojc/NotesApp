package com.example.notesapp.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.notesapp.ui.theme.Blue
import com.example.notesapp.ui.theme.Green
import com.example.notesapp.ui.theme.Red
import com.example.notesapp.ui.theme.Violet
import com.example.notesapp.ui.theme.Yellow

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("categoryId"),
            childColumns = arrayOf("categoryId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Priority::class,
            parentColumns = arrayOf("priorityId"),
            childColumns = arrayOf("priorityId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Note (
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val categoryId: Int,
    val priorityId: Int,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int
) {
    companion object {
        val colors = listOf(Red, Yellow, Green, Blue, Violet)
    }
}
