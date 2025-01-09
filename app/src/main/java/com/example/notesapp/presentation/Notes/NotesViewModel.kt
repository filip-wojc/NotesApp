package com.example.notesapp.presentation.notes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.use_cases.categories.CategoryUseCases
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
class NotesViewModel @Inject constructor(
    private val _noteUseCases: NoteUseCases,
    private val _categoryUseCases: CategoryUseCases
) : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())

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

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _defaultCategory = Category("All Notes", 0)

    private val _currentCategory = MutableStateFlow(_defaultCategory)
    val currentCategory : StateFlow<Category> = _currentCategory

    init {
        getNotes()
        getCategories()
    }


    fun getNotes(){
        viewModelScope.launch{
            _noteUseCases.getAllNotes(
                noteOrder = gerOrderType()
            ).collect { noteList ->
                _notes.value = noteList
                _allNotes.value = noteList
                searchBarOnValueChange(_searchText.value)
                _currentCategory.value = _defaultCategory
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categoryUseCases.getAllCategories().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }

    fun getNotesByCategory(category: Category) {
        viewModelScope.launch {
            _noteUseCases.getNotesByCategory(
                noteOrder = gerOrderType(),
                categoryId = category.categoryId
            ).collect { noteList ->
                _notes.value = noteList
                _allNotes.value = noteList
                searchBarOnValueChange(_searchText.value)
                _currentCategory.value = category
            }
        }
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
        if (_currentCategory.value.name != "All Notes") {
            getNotesByCategory(_currentCategory.value)
        }
        else {
            getNotes()
        }
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
            if (_currentCategory.value.name != "All Notes") {
                getNotesByCategory(_currentCategory.value)
            }
            else {
                getNotes()
            }
        }
    }

    fun onSelectedSort(sortMethod: String) {
        _currentSortMethod.value = sortMethod
        if (_currentCategory.value.name != "All Notes") {
            getNotesByCategory(_currentCategory.value)
        }
        else {
            getNotes()
        }
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