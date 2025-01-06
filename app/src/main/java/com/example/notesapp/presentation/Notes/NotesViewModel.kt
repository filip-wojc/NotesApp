package com.example.notesapp.presentation.notes

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())

    private val _isMenuVisible = MutableStateFlow(false)
    val isMenuVisible : StateFlow<Boolean> = _isMenuVisible

    private val _isSearchBarVisible = MutableStateFlow(false)
    val isSearchBarVisible : StateFlow<Boolean> = _isSearchBarVisible

    private val _searchText = MutableStateFlow("")
    val searchText : StateFlow<String> = _searchText

    private val _currentSortMethod = MutableStateFlow("Date")
    val currentSortMethod : StateFlow<String> = _currentSortMethod

    private val _isSortMethodVisible = MutableStateFlow(false)
    val isSortMethodVisible : StateFlow<Boolean> = _isSortMethodVisible

    private val _isOrderDescending = MutableStateFlow(true)
    val isOrderDescending : StateFlow<Boolean> = _isOrderDescending

    private val _isDeleting = MutableStateFlow(false)
    val isDeleting : StateFlow<Boolean> = _isDeleting

    init {
        getNotes()
    }


    private fun getNotes(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)){
        viewModelScope.launch{
            _noteUseCases.getAllNotes(noteOrder).collect { noteList ->
                _notes.value = noteList
                _allNotes.value = noteList
                searchBarOnValueChange(_searchText.value)
            }
        }
    }

    fun toggleMenuVisibility() {
        _isMenuVisible.value = !_isMenuVisible.value
    }

    fun toggleSearchBarVisibility() {
        _isSearchBarVisible.value = !_isSearchBarVisible.value
    }

    fun toggleSortMethodsVisibility() {
        _isSortMethodVisible.value = !_isSortMethodVisible.value
    }

    fun toggleDeleting() {
        _isDeleting.value = !_isDeleting.value
    }

    fun toggleSortDirection() {
        _isOrderDescending.value = !_isOrderDescending.value
        getNotes(gerOrderType())
    }

    fun searchBarOnValueChange(value: String) {
        _searchText.value = value
        if (value.isNotEmpty()) {
            _notes.value =
                _allNotes.value.filter {
                    it.title.contains(value, ignoreCase = true) || it.content.contains(value, ignoreCase = true)
        }
        } else {
            _notes.value = _allNotes.value
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            _noteUseCases.deleteNotes(note)
            _notes.value = notes.value.filter { it != note }
            _allNotes.value = notes.value.filter { it != note }
        }
    }

    fun onSelectedSort(sortMethod: String) {
        _currentSortMethod.value = sortMethod
        getNotes(gerOrderType())
    }

    private fun gerOrderType() : NoteOrder {
        return if (_isOrderDescending.value) {
            when (_currentSortMethod.value) {
                "Date" -> return NoteOrder.Date(OrderType.Descending)
                "Title" -> return NoteOrder.Title(OrderType.Descending)
                "Category" -> return NoteOrder.Category(OrderType.Descending)
                "Priority" -> return NoteOrder.Priority(OrderType.Descending)
                "Color" -> return NoteOrder.Color(OrderType.Descending)
                else -> NoteOrder.Date(OrderType.Descending)
            }
        }
        else {
            when (_currentSortMethod.value) {
                "Date" -> NoteOrder.Date(OrderType.Ascending)
                "Title" -> NoteOrder.Title(OrderType.Ascending)
                "Category" -> NoteOrder.Category(OrderType.Ascending)
                "Priority" -> NoteOrder.Priority(OrderType.Ascending)
                "Color" -> NoteOrder.Color(OrderType.Ascending)
                else -> NoteOrder.Date(OrderType.Ascending)
            }
        }
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