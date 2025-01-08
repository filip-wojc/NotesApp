package com.example.notesapp.presentation._common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Priority

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PickerDialog(
    onDismiss: () -> Unit,
    onSave: (Priority, Category) -> Unit,
    priorities: List<Priority>,
    categories: List<Category>,
    onAddCategory: (Category) -> Unit
) {
    var selectedPriority by remember { mutableStateOf(priorities.firstOrNull() ?: Priority("", 0))}
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: Category("",0))}
    var newCategory by remember { mutableStateOf(Category("",0)) }

    var prioritiesExpanded by remember { mutableStateOf(false)}
    var categoriesExpanded by remember { mutableStateOf(false)}

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = { onSave(selectedPriority, selectedCategory) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        },
        title = { Text("Select Priority and Category") },
        // Pickers
        text = {
            Column {

                // Priority Picker
                ExposedDropdownMenuBox(
                    expanded = prioritiesExpanded,
                    onExpandedChange = { prioritiesExpanded = it }
                )
                {

                    OutlinedTextField(
                        value = selectedPriority.name,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        label = { Text("Priority") },
                        trailingIcon =
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dropdown),
                                contentDescription = "Expand Priority",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = prioritiesExpanded,
                        onDismissRequest = { prioritiesExpanded = false }
                    )
                    {
                        priorities.forEach { priority ->
                            DropdownMenuItem(
                                text = { Text(priority.name) },
                                onClick = {
                                    selectedPriority = priority
                                    prioritiesExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Picker

                ExposedDropdownMenuBox(
                    expanded = categoriesExpanded,
                    onExpandedChange = { categoriesExpanded = it }
                )
                {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        label = { Text("Category") },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_dropdown),
                                contentDescription = "Expand Category",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = categoriesExpanded,
                        onDismissRequest = { categoriesExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    categoriesExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Add New Category Input
                OutlinedTextField(
                    value = newCategory.name,
                    onValueChange = { newCategory = Category(it, 0) },
                    label = { Text("Add New Category") },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (newCategory.name.isNotBlank()) {
                                onAddCategory(newCategory)
                                newCategory = Category("",0)// wipe data
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Add Category"
                            )
                        }
                    }
                )
            }
        }
    )
}