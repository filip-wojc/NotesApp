package com.example.notesapp.domain.repositories

import com.example.notesapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes() : Flow<List<Note>>

    fun getNotesByCategoryId(categoryId: Int): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun upsertNote(note: Note)

    suspend fun deleteNote(note: Note)
}