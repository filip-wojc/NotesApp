package com.example.notesapp.presentation._common.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.notesapp.R
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.utils.Notifications.ReminderScheduler
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun PickerDialog(
    onDismiss: () -> Unit,
    onSave: (Priority, Category, reminderTime: Long? ) -> Unit,
    priorities: List<Priority>,
    categories: List<Category>,
    onAddCategory: (Category) -> Unit
) {

    // priority variables
    var selectedPriority by remember { mutableStateOf(priorities.firstOrNull() ?: Priority("", 0))}
    var prioritiesExpanded by remember { mutableStateOf(false)}

    // category variables
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: Category("",0))}
    var categoriesExpanded by remember { mutableStateOf(false)}
    var newCategory by remember { mutableStateOf(Category("",0)) }

    // reminder variables
    val context = LocalContext.current
    val currentDateTime = remember { java.util.Calendar.getInstance() } // Current date + current time
    val reminderPermissionsGranted = remember { mutableStateOf(false) }

    // date
    val dateFormatter = remember { java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()) }
    var isReminderSet by remember { mutableStateOf(false)}
    var setReminderDate by remember { mutableStateOf<Long?>(null) }

    // time
    val timeFormatter = remember { java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()) }

    // THEME
    val isDarkTheme = isSystemInDarkTheme()

    val themeResId = if (isDarkTheme) {
        android.R.style.Theme_DeviceDefault_Dialog
    } else {
        android.R.style.Theme_DeviceDefault_Light_Dialog
    }



    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            reminderPermissionsGranted.value = isGranted
        }
    )

    // Trigger permission request when the dialog is first shown
    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permissionGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            reminderPermissionsGranted.value = permissionGranted

            if (!permissionGranted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    AlertDialog(

        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                ),
                onClick = {
                    // init with correct value depending on the current reminder time variable state
                    val reminderTime = if (isReminderSet && setReminderDate != null && setReminderDate!! > System.currentTimeMillis()) {
                        setReminderDate
                    } else
                    {
                        null
                    }

                    if (reminderTime != null) {
                        ReminderScheduler.scheduleReminder(
                            context = context,
                            reminderTime = reminderTime,
                            title = "Reminder",
                            message = "Don't forget to check your note in ${selectedCategory.name}!"
                        )
                    }

                    onSave(selectedPriority, selectedCategory, reminderTime)
                },
                // validate whether date is correct
                enabled = !isReminderSet || (setReminderDate != null && setReminderDate!! > System.currentTimeMillis()) // Validate reminder
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary
                ),
            ) {
                Text("Cancel")
            }
        },
        title = { Text("Select Priority and Category") },
        // Pickers
        text = {
            Column() {

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
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )

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
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )
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
                        }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "Add Category")
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Set reminder checkbox
                Box(modifier = Modifier
                    .then(
                        if (reminderPermissionsGranted.value) {
                            Modifier.clickable { isReminderSet = !isReminderSet }
                        } else {
                            Modifier // No clickable modifier when permission is not granted
                        }
                    )
                    .align(Alignment.CenterHorizontally)
                    .padding(top=10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Align items in the center vertically
                        horizontalArrangement = Arrangement.Start // Ensure checkbox and text are aligned left
                    ) {
                        Checkbox(
                            checked = isReminderSet,
                            onCheckedChange = {
                                isReminderSet = it
                                              }, // Keep checkbox state in 1sync
                            enabled = reminderPermissionsGranted.value
                            )
                        Text("Set reminder")
                    }
                }


                // Reminder Date + Time Picker
                if (isReminderSet) {
                    val formattedDate = setReminderDate?.let { dateFormatter.format(it) }
                        ?: dateFormatter.format(currentDateTime.timeInMillis)
                    val formattedTime = setReminderDate?.let { timeFormatter.format(it) }
                        ?: timeFormatter.format(currentDateTime.timeInMillis)

                    // Date Picker
                    OutlinedTextField(
                        value = formattedDate,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        label = { Text("Reminder Date") },
                        trailingIcon = {
                            IconButton(onClick = {
                                val datePicker = android.app.DatePickerDialog(
                                    context,
                                    themeResId,
                                    { _, year, month, dayOfMonth ->
                                        currentDateTime.set(java.util.Calendar.YEAR, year)
                                        currentDateTime.set(java.util.Calendar.MONTH, month)
                                        currentDateTime.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
                                        setReminderDate = currentDateTime.timeInMillis // Update timestamp
                                    },
                                    currentDateTime.get(java.util.Calendar.YEAR),
                                    currentDateTime.get(java.util.Calendar.MONTH),
                                    currentDateTime.get(java.util.Calendar.DAY_OF_MONTH)
                                )
                                datePicker.show()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar_64),
                                    contentDescription = "Pick Date"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Time Picker
                    OutlinedTextField(
                        value = formattedTime,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        label = { Text("Reminder Time") },
                        trailingIcon = {
                            IconButton(onClick = {
                                val timePicker = android.app.TimePickerDialog(
                                    context,
                                    themeResId,
                                    { _, hour, minute ->
                                        currentDateTime.set(java.util.Calendar.HOUR_OF_DAY, hour)
                                        currentDateTime.set(java.util.Calendar.MINUTE, minute)
                                        currentDateTime.set(java.util.Calendar.SECOND, 0) // Clear seconds
                                        setReminderDate = currentDateTime.timeInMillis // Update timestamp
                                    },
                                    currentDateTime.get(java.util.Calendar.HOUR_OF_DAY),
                                    currentDateTime.get(java.util.Calendar.MINUTE),
                                    true // Use 24-hour format
                                )
                                timePicker.show()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_time_64),
                                    contentDescription = "Pick Time"
                                )
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    )
}