package com.example.notesapp.presentation.notes
import com.example.notesapp.presentation.notes.composables.TopBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notesapp.presentation.notes.NotesViewModel
import com.example.notesapp.presentation.notes.composables.NotePreview
import com.example.notesapp.presentation.notes.composables.SearchingBar


@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {

    val notes = viewModel.notes
    val isMenuVisible = viewModel.isMenuVisible.collectAsState()
    val isSearchBarVisible = viewModel.isSearchBarVisible.collectAsState()
    val isSortMethodsVisible = viewModel.isSortMethodVisible.collectAsState()
    val searchText = viewModel.searchText.collectAsState()
    val currentSortMethod = viewModel.currentSortMethod.collectAsState()
    val isSortDescending = viewModel.isSortDescending.collectAsState()
    val isDeleting = viewModel.isDeleting.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
          currentFilter = "All notes",
            toggleDeleting = {viewModel.toggleDeleting()},
            toggleMenuVisibility = {viewModel.toggleMenuVisibility()},
            toggleSearchBarVisibility = {viewModel.toggleSearchBarVisibility()},
            isDeleting = isDeleting.value
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = {viewModel.toggleSortMethodsVisibility()},
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "SortButton",
                )
            }
            Text(
                text = currentSortMethod.value,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "|",
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(
                onClick = {viewModel.toggleSortDirection()},
            ) {
                Icon(
                    imageVector = if (isSortDescending.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = "SortButton",
                )
            }
        }

        if (isSearchBarVisible.value) {
            SearchingBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = searchText.value,
                onValueChange = {viewModel.searchBarOnValueChange(it)}
            )
        }

        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(notes.chunked(2)) { rowNotes ->
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
                            modifier = Modifier
                                .weight(1f),
                            isDeleting = isDeleting.value,
                            onDelete = {viewModel.deleteNote(note)}
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
