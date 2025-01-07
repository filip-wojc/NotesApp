package com.example.notesapp.presentation.CreateNote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.use_cases.categories.CategoryUseCases
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CreateNoteViewModel @Inject constructor(private val _noteUseCases: NoteUseCases, private val _categoryUseCases: CategoryUseCases)  : ViewModel() {
    private val _title = MutableStateFlow("")
    val title : StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedColor = MutableStateFlow(Color.White) // Default color
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _priorities = mutableStateListOf<Priority>()
    val priorities: List<Priority> = _priorities

    private val _categories = mutableStateListOf<Category>()
    val categories: List<Category> = _categories

    var selectedPriority by mutableStateOf(Priority("",0))
    var selectedCategory by mutableStateOf(Category("",0))

    init {
        fetchCategories()
        fetchPriorities()

    }

    fun updateTitle(newTitle: String){
        if (newTitle.length <= 30) { // Replace 50 with your desired character limit
            _title.value = newTitle
        }
    }

    fun updateDescription(newDescription: String){
        _description.value = newDescription
    }

    fun updateColor(newColor: Color){
        _selectedColor.value = newColor
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _categoryUseCases.getAllCategories().collect{
                categoriesList ->
                _categories.clear()
                _categories.addAll(categoriesList)
            }

        }
    }

    fun fetchPriorities()
    {
        val p1 = Priority("Low", 1)
        val p2 = Priority("Medium", 2)
        val p3 = Priority("High", 3)

        val temp = mutableListOf(p1,p2,p3)

        _priorities.clear()
        _priorities.addAll(temp)


    }



    fun addCategory(newCategory: Category) {
        viewModelScope.launch {
            _categoryUseCases.upsertCategory(newCategory)
            fetchCategories() // Refresh categories
        }
    }

    fun updateSelectedPriority(priority: Priority) {
        selectedPriority = priority
    }

    fun updateSelectedCategory(category: Category) {
        selectedCategory = category
    }

}