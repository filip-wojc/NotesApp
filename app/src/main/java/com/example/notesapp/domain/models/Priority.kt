package com.example.notesapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Priority (
    val name: String,
    @PrimaryKey(autoGenerate = true)
    val priorityId: Int
)