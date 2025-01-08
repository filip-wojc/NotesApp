package com.example.notesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.CategoryRepository
import com.example.notesapp.domain.repositories.PriorityRepository
import com.example.notesapp.presentation.CreateNote.CreateNoteScreen
import com.example.notesapp.presentation.notes.NotesScreen
import com.example.notesapp.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable




@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            NotesAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NoteListScreen.route
                ) {
                    composable(
                        route = NoteListScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { -300 },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { -300 },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        NotesScreen(navController = navController)
                    }

                    composable(
                        route = CreateNoteScreen.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { 300 },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { -300 },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        CreateNoteScreen(navController = navController)
                    }
                }

            }
        }
    }
}

@Serializable
object NoteListScreen {
    const val route = "noteListScreen"
}

@Serializable
object CreateNoteScreen {
    const val route = "createNoteScreen"
}