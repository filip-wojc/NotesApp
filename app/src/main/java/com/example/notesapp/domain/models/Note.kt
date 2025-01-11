package com.example.notesapp.domain.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.notesapp.ui.theme.Black

import com.example.notesapp.ui.theme.BlueViolet
import com.example.notesapp.ui.theme.DarkBlue
import com.example.notesapp.ui.theme.DarkGreen
import com.example.notesapp.ui.theme.DarkOrange
import com.example.notesapp.ui.theme.DarkRed
import com.example.notesapp.ui.theme.DarkViolet
import com.example.notesapp.ui.theme.Grey
import com.example.notesapp.ui.theme.LightBlue
import com.example.notesapp.ui.theme.LightGreen
import com.example.notesapp.ui.theme.MediumBlue
import com.example.notesapp.ui.theme.Orange
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
    val reminderDate: Long?,
    val color: Int,
    val categoryId: Int,
    val priorityId: Int,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int
) {
    companion object {
        val colors = listOf(Yellow, Orange, DarkOrange, Red, DarkRed, LightGreen, DarkGreen,
            LightBlue, BlueViolet, Violet, MediumBlue, DarkBlue, DarkViolet, Grey)
    }
}
