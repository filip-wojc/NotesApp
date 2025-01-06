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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
    val searchText = viewModel.searchText.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopBar(
          currentFilter = "All notes",
            toggleDeleting = {},
            toggleMenuVisibility = {viewModel.toggleMenuVisibility()},
            toggleSearchBarVisibility = {viewModel.toggleSearchBarVisibility()},
            isVisible = isSearchBarVisible.value
        )
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
                                .weight(1f)
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
