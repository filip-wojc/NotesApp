package com.example.notesapp.presentation.notes

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val _noteUseCases: NoteUseCases) : ViewModel() {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    private val _isMenuVisible = MutableStateFlow(false)
    val isMenuVisible : StateFlow<Boolean> = _isMenuVisible

    private val _isSearchBarVisible = MutableStateFlow(false)
    val isSearchBarVisible : StateFlow<Boolean> = _isSearchBarVisible

    private val _searchText = MutableStateFlow("")
    val searchText : StateFlow<String> = _searchText

    init {
        getNotes()
    }

    private fun getNotes(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Ascending)){
        viewModelScope.launch{
            _noteUseCases.getAllNotes(noteOrder).collect { noteList ->
                _notes.clear()
                _notes.addAll(noteList)
            }
        }
    }

    fun toggleMenuVisibility() {
        _isMenuVisible.value = !_isMenuVisible.value
    }

    fun toggleSearchBarVisibility() {
        _isSearchBarVisible.value = !_isSearchBarVisible.value
    }

    fun searchBarOnValueChange(value: String) {
        _searchText.value = value
    }

    @SuppressLint("NewApi")
    fun formatTimeStamp(timestamp: Long) : String {
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        return localDateTime.format(formatter)
    }

}