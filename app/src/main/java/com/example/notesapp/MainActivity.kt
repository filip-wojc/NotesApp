package com.example.notesapp

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.database.daos.PriorityDao
import com.example.notesapp.database.repositories.PriorityRepositoryImpl
import com.example.notesapp.di.AppModule
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.PriorityRepository
import com.example.notesapp.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


//Main activity tymczasowo zmieniony zeby sprawdzic czy baza
//sie generuje
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repo: PriorityRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            repo.upsertPriority(Priority("Low", 1))
            repo.upsertPriority(Priority("Medium", 2))
            repo.upsertPriority(Priority("High", 3))
        }
        setContent {
            NotesAppTheme {
               Text("Hello World")
            }
        }
    }
}
