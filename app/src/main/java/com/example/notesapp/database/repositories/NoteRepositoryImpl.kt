package com.example.notesapp.database.repositories

import com.example.notesapp.database.daos.NoteDao
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.repositories.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes()
    }

    override fun getNotesByCategoryId(categoryId: Int): Flow<List<Note>> {
        return dao.getNotesByCategoryId(categoryId)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun upsertNote(note: Note) {
        dao.upsertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

}