package com.example.notesapp.presentation.notes.composables


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesapp.R

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    currentFilter: String,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    toggleMenuVisibility: () -> Unit,
    toggleSearchBarVisibility: () -> Unit,
    toggleDeleting: () -> Unit,
    isDeleting: Boolean,
    isSearching: Boolean
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ){
            IconButton(
                onClick = toggleMenuVisibility,
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu button",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            AnimatedVisibility(
                visible = isSearching
            ) {
                SearchingBar(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier.fillMaxWidth()

                )
            }

            if (!isSearching) {
                Text(currentFilter, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
            }


        }
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(0.3f)
        ) {
            IconButton(
                onClick = toggleSearchBarVisibility,
            ) {
                if (!isSearching) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Toggle Searching",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.baseline_search_off_24),
                        contentDescription = "Toggle",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            IconButton(
                onClick = toggleDeleting,
            ) {
                Icon(
                    imageVector = if (!isDeleting) Icons.Filled.Delete else Icons.Filled.Check,
                    contentDescription = "Toggle delete",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}