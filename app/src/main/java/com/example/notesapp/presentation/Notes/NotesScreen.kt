package com.example.notesapp.presentation.notes
import android.util.Log
import com.example.notesapp.presentation.notes.composables.TopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesapp.R
import com.example.notesapp.domain.models.Category
import com.example.notesapp.presentation.notes.NotesViewModel
import com.example.notesapp.presentation.notes.composables.NotePreview
import com.example.notesapp.presentation.notes.composables.SearchingBar
import com.example.notesapp.presentation.notes.composables.SortMethodList
import com.example.notesapp.ui.theme.Background
import com.example.notesapp.ui.theme.LightYellow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val notes = viewModel.notes.collectAsState()
    val isDeleting = viewModel.isDeleting.collectAsState()
    val searchText = viewModel.searchText.collectAsState()
    val currentSortMethod = viewModel.currentSortMethod.collectAsState()
    val isOrderDescending = viewModel.isOrderDescending.collectAsState()
    val isSearchBarVisible = viewModel.isSearchBarVisible.collectAsState()
    val isSortMethodsVisible = viewModel.isSortMethodVisible.collectAsState()
    val categories = viewModel.categories.collectAsState()
    val currentCategory = viewModel.currentCategory.collectAsState()

    // Stan dla Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(200.dp)
            ){
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                categories.value.forEach { category ->
                    TextButton(
                        onClick = {
                            viewModel.getNotesByCategory(category)
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                HorizontalDivider()
                TextButton(
                    onClick = {
                        viewModel.getNotes()
                        coroutineScope.launch { drawerState.close() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "All Notes",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            TopBar(
                currentFilter = currentCategory.value.name,
                toggleDeleting = { viewModel.toggleDeleting() },
                toggleMenuVisibility = {
                    coroutineScope.launch { drawerState.open() }
                },
                toggleSearchBarVisibility = { viewModel.toggleSearchBarVisibility() },
                isDeleting = isDeleting.value,
                isSearching = isSearchBarVisible.value
            )

            // Sortowanie i wyszukiwanie
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.toggleSortMethodsVisibility() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_sort_24),
                        contentDescription = "SortButton",
                    )
                }
                Text(
                    text = currentSortMethod.value,
                    style = MaterialTheme.typography.bodyMedium,
                )
                IconButton(
                    onClick = { viewModel.toggleSortDirection() },
                ) {
                    Icon(
                        imageVector = if (isOrderDescending.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "SortButton",
                    )
                }
            }

            if (isSortMethodsVisible.value) {
                SortMethodList(
                    onSelectSort = { viewModel.onSelectedSort(it) }
                )
            }

            if (isSearchBarVisible.value) {
                SearchingBar(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    value = searchText.value,
                    onValueChange = { viewModel.searchBarOnValueChange(it) }
                )
            }

            // Lista notatek
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(notes.value.chunked(2)) { rowNotes ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (note in rowNotes) {
                            NotePreview(
                                note = note,
                                noteFormattedDate = viewModel.formatTimeStamp(note.timestamp),
                                modifier = Modifier.weight(1f),
                                isDeleting = isDeleting.value,
                                onDelete = { viewModel.deleteNote(note) },
                                onClick = {}
                            )
                        }
                        if (rowNotes.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
