package com.example.notesapp.presentation.notes.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    currentFilter: String,
    toggleMenuVisibility: () -> Unit,
    toggleSearchBarVisibility: () -> Unit,
    toggleDeleting: () -> Unit,
    isDeleting: Boolean
) {
    Row (
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(0.3f)
        ){
            IconButton(
                onClick = toggleMenuVisibility,
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu button",
                )
            }
            Text(currentFilter, fontSize = 20.sp)
        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(0.2f)
        ) {
            IconButton(
                onClick = toggleSearchBarVisibility,
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Toogle Searching"
                )
            }
            IconButton(
                onClick = toggleDeleting,
            ) {
                Icon(
                    imageVector = if (!isDeleting) Icons.Filled.Delete else Icons.Filled.Check,
                    contentDescription = "Toogle delete"
                )
            }
        }
    }
}