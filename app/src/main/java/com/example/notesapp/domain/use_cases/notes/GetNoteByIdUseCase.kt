package com.example.notesapp.domain.use_cases.notes

import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.repositories.NoteRepository

class GetNoteByIdUseCase (
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
       return repository.getNoteById(id)
    }
}