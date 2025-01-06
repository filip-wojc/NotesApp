package com.example.notesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.CategoryRepository
import com.example.notesapp.domain.repositories.PriorityRepository
import com.example.notesapp.presentation.notes.NotesScreen
import com.example.notesapp.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject



@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            NotesAppTheme {
                NotesScreen()
            }
        }
    }
}
