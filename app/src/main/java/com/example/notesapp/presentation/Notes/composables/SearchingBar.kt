package com.example.notesapp.presentation.notes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notesapp.ui.theme.LightYellow

@Composable
fun SearchingBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (text: String) -> Unit
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ){
        TextField(
            value = value,
            onValueChange = {onValueChange(it)},
            placeholder = { Text("Search...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search icon") },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                cursorColor = Color.Black,
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),

        )
    }
}