package com.example.notesapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notesapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("Select * From note")
    fun getAllNotes(): Flow<List<Note>>

    @Query("Select * From note Where categoryId=:categoryId")
    fun getNotesByCategoryId(categoryId: Int): Flow<List<Note>>

    @Query("Select * From note Where priorityId=:priorityId")
    fun getNotesByPriorityId(priorityId: Int): Flow<List<Note>>

    @Query("Select * from note where noteId=:id")
    suspend fun getNoteById(id: Int) : Note?

    @Upsert
    suspend fun upsertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}