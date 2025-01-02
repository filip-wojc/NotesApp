package com.example.notesapp.domain.use_cases.notes

data class NoteUseCases (
    val getAllNotes : GetAllNotesUseCase,
    val getNotesByCategory: GetNotesByCategoryIdUseCase,
    val getNotesByPriority: GetNotesByPriorityIdUseCase,
    val getNote: GetNoteByIdUseCase,
    val upsertNote: UpsertNoteUseCase,
    val deleteNotes: DeleteNoteUseCase,
)