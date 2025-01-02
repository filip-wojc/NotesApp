package com.example.notesapp.domain.use_cases.notes

data class NoteUseCases (
    val getAllNotes : GetAllNotesUseCase,
    val deleteNotes: DeleteNoteUseCase
)