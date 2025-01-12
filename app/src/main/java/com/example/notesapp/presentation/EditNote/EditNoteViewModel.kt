package com.example.notesapp.presentation.EditNote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.use_cases.categories.CategoryUseCases
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import com.example.notesapp.domain.use_cases.priorities.PriorityUseCases
import com.example.notesapp.domain.utils.VoiceToTextParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val _noteUseCases: NoteUseCases,
    private val _categoryUseCases: CategoryUseCases,
    private val _priorityUseCases: PriorityUseCases,
    val voiceToTextParser: VoiceToTextParser
)  : ViewModel() {
    private val _title = MutableStateFlow("")
    val title : StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedColor = MutableStateFlow(Color.White)
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _priorities = mutableStateListOf<Priority>()
    val priorities: List<Priority> = _priorities

    private val _categories = mutableStateListOf<Category>()
    val categories: List<Category> = _categories

    private val _noteId = mutableStateOf<Int>(0)

    var selectedPriority by mutableStateOf(Priority("",0))
    var selectedCategory by mutableStateOf(Category("",0))

    private val _reminderTime = mutableStateOf<Long?>(null) // dont share with screen, read-only

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

        val text = newText.replaceFirstChar { it.uppercase() }

        if (newText.lowercase() == "clear description") {
            _description.value = ""
        }

        else if (newText.lowercase() == "clear title") {
            _title.value = ""
        }

        else if (!_description.value.endsWith(" ") && _description.value.isNotEmpty()) {
            _description.value += " $text."
        }
        else {
            _description.value += "$text."
        }
    }

    fun updateReminderTime(time: Long?){
        _reminderTime.value = time
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

    fun fetchCategories() {
        viewModelScope.launch {
            _categoryUseCases.getAllCategories().collect{ fetchedCategories ->
                if(fetchedCategories.isEmpty()){
                    val c1 = Category("General",1)
                    val c2 = Category("Important", 2)
                    val c3 = Category("Other", 3)
                    _categoryUseCases.upsertCategory(c1)
                    _categoryUseCases.upsertCategory(c2)
                    _categoryUseCases.upsertCategory(c3)
                    _categories.clear()
                    _categories.addAll(listOf(c1,c2,c3))
                }
                else{
                    _categories.clear()
                    _categories.addAll(fetchedCategories)
                }
            }
        }
    }

    fun fetchPriorities()
    {
        viewModelScope.launch {
            _priorityUseCases.getAllPriorities().collect{ fetchedPriorities ->
                // Initialize priorities if db empty
                if(fetchedPriorities.isEmpty()){
                    val p1 = Priority("Low", 1)
                    val p2 = Priority("Medium", 2)
                    val p3 = Priority("High", 3)
                    _priorityUseCases.upsertPriority(p1)
                    _priorityUseCases.upsertPriority(p2)
                    _priorityUseCases.upsertPriority(p3)
                    _priorities.clear()
                    _priorities.addAll(listOf(p1,p2,p3))
                }
                else{
                    _priorities.clear()
                    _priorities.addAll(fetchedPriorities)
                }
            }
        }
    }

    fun addCategory(newCategory: Category) {
        viewModelScope.launch {
            _categoryUseCases.upsertCategory(newCategory)
            fetchCategories() // Refresh categories
        }
    }

    fun saveNote(){
        Log.d("SelectedCategorySave" ,"BeforeSaveSelectedName:${selectedCategory.name},BeforeSaveSelectedId:${selectedCategory.categoryId}")
        val note = Note(
            _title.value,
            _description.value,
            System.currentTimeMillis(),
            _reminderTime.value,
            _selectedColor.value.toArgb(),
            selectedCategory.categoryId,
            selectedPriority.priorityId,
            _noteId.value
        )

        viewModelScope.launch {
            _noteUseCases.upsertNote(note)
        }
    }

}