package com.example.notesapp.presentation.Notes

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import com.example.notesapp.domain.utils.NoteOrder
import com.example.notesapp.domain.utils.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val _noteUseCases: NoteUseCases) : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    init {
        getNotes()
    }

    fun getNotes(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)){
        viewModelScope.launch{
            _noteUseCases.getAllNotes(noteOrder).collect { noteList ->
                _notes.clear()
                _notes.addAll(noteList)
            }
        }
    }

    fun addOrUpdateNote(note: Note){
        viewModelScope.launch {
            try{
                _noteUseCases.upsertNote(note)
                getNotes()
            } catch (e:Exception){
                Log.e("addNote", e.message.toString())
            }
        }
    }



}