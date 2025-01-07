package com.example.notesapp.presentation.notes.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.ui.theme.LightYellow

@Composable
fun SortMethodList(
    onSelectSort: (word: String) -> Unit,
    currentSort: String
) {
    val sortMethods1 = listOf("Title", "Date", "Color")
    val sortMethods2 = listOf("Priority", "Category")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sortMethods1.forEach { method ->
                val isSelected = currentSort == method
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightYellow, RoundedCornerShape(12.dp))
                        .then(
                            if (isSelected) Modifier.border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            else Modifier
                        )
                        .clickable { onSelectSort(method) }
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                ) {
                    Text(text = method)
                }
            }
        }

        Spacer(modifier = Modifier.padding(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            sortMethods2.forEach { method ->
                val isSelected = currentSort == method
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightYellow, RoundedCornerShape(12.dp))
                        .then(
                            if (isSelected) Modifier.border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            else Modifier
                        )
                        .clickable { onSelectSort(method) }
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                ) {
                    Text(text = method)
                }
            }
        }
    }
}


