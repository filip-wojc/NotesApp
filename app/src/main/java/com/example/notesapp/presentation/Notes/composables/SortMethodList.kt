package com.example.notesapp.presentation.notes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SortMethodList(
    onSelectSort: (word: String) -> Unit,
) {
    val sortMethods = listOf("Title", "Date", "Color", "Priority", "Category")
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        sortMethods.forEach {
            Box(
                modifier = Modifier
                    .background(com.example.notesapp.ui.theme.LightYellow, RoundedCornerShape(12.dp))
                    .clickable {onSelectSort(it)}
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = it,
                )
            }

        }
    }
}
