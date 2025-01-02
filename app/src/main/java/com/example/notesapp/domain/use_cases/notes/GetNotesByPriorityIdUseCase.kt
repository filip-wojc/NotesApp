package com.example.notesapp.domain.use_cases.notes

import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.repositories.NoteRepository
import com.example.notesapp.domain.utils.NoteOrder
import com.example.notesapp.domain.utils.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesByPriorityIdUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        priorityId: Int
    ): Flow<List<Note>> {
        return repository.getNotesByCategoryId(priorityId).map {notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                        is NoteOrder.Priority -> notes.sortedBy { it.priorityId }
                        is NoteOrder.Category -> notes.sortedBy { it.categoryId }
                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                        is NoteOrder.Priority -> notes.sortedByDescending { it.priorityId }
                        is NoteOrder.Category -> notes.sortedByDescending { it.categoryId }
                    }
                }
            }
        }
    }
}