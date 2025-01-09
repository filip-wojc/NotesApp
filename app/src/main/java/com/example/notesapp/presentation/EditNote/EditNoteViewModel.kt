package com.example.notesapp.presentation.EditNote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.use_cases.categories.CategoryUseCases
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import com.example.notesapp.domain.utils.VoiceToTextParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val _noteUseCases: NoteUseCases,
    private val _categoryUseCases: CategoryUseCases,
    val voiceToTextParser: VoiceToTextParser
)  : ViewModel() {
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

    private val _noteId = mutableStateOf<Int>(0)

    var selectedPriority:Priority? by mutableStateOf(Priority("",0))
    var selectedCategory:Category? by mutableStateOf(Category("",0))

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

    fun updateDescriptionWithVoice(newText: String) {
        if (newText.lowercase() == "clear description") {
            _description.value = ""
        }

        else if (newText.lowercase() == "clear title") {
            _title.value = ""
        }

        else if (!_description.value.endsWith(" ") && _description.value.isNotEmpty()) {
            _description.value += " $newText"
        }
        else {
            _description.value += newText
        }
    }

    fun updateColor(newColor: Color){
        _selectedColor.value = newColor
    }

    fun updateSelectedPriority(priority: Priority) {
        selectedPriority = priority
    }

    fun updateSelectedCategory(category: Category) {
        selectedCategory = category
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

    fun loadNoteInfo(noteId: Int){

        viewModelScope.launch {
            val note = _noteUseCases.getNote(noteId)


            note?.let {
                // Populate values from note if it exists
                _title.value = it.title
                _description.value = it.content
                _selectedColor.value = Color(it.color)
                selectedCategory = categories.find { category ->
                    category.categoryId == it.categoryId
                } ?: Category("", 0) //

                selectedPriority = priorities.find { priority ->
                    priority.priorityId == it.priorityId
                } ?: Priority("Low", 1)

                _noteId.value = it.noteId
            } ?: run {
                // Fallback to empty/default values
                _title.value = ""
                _description.value = ""
                _selectedColor.value = Color.White
                selectedCategory = Category("", 0)
                selectedPriority = Priority("", 0)
                _noteId.value = 0
            }

        }




    }

    fun saveNote(){
        val note = Note(
            _title.value,
            _description.value,
            System.currentTimeMillis(),
            _selectedColor.value.toArgb(),
            selectedCategory!!.categoryId,
            selectedPriority!!.priorityId,
            _noteId.value
        )

        viewModelScope.launch {
            _noteUseCases.upsertNote(note)
        }
    }





}