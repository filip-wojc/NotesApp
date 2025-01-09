package com.example.notesapp.presentation.CreateNote

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.notesapp.R
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.utils.VoiceToTextParserState
import com.example.notesapp.presentation._common.composables.PickerDialog
import com.example.notesapp.ui.theme.*


@Composable
fun CreateNoteScreen(
    viewModel: CreateNoteViewModel = hiltViewModel(),
    navController: NavController
) {

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                viewModel.voiceToTextParser.updateState {
                    it.copy(error = "Permission for microphone is required")
                }

            }
        }
    )

    val context = LocalContext.current
    val hasPermission = remember {
        androidx.core.content.ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    if (!hasPermission) {
        LaunchedEffect(Unit) {
            requestPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        }
    }



    val title = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()
    val selectedColor = viewModel.selectedColor.collectAsState()
    val priorities = viewModel.priorities
    val categories = viewModel.categories


    val colors = Note.colors
    val rows = colors.chunked(7)
    var showDialog by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    val speechState by viewModel.voiceToTextParser.state.collectAsState()

    LaunchedEffect(speechState.spokenText) {
        if (speechState.spokenText.isNotEmpty()) {
            viewModel.updateDescriptionWithVoice(speechState.spokenText)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor.value)
            .padding(6.dp)
    )
    {


        // Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp,2.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            IconButton(onClick = {navController.popBackStack()})
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back Arrow"
                )
            }

            Box(modifier = Modifier.weight(1f))
            {
                BasicTextField(
                    value = title.value,
                    onValueChange = { viewModel.updateTitle(it) },
                    textStyle = TextStyle(
                        fontSize = 25.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(end = 50.dp)
                )
                Text(
                    text = "${title.value.length}/30",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

        }


        HorizontalDivider(color = Black)
        Spacer(modifier = Modifier.padding(2.dp))

        // Color picker
        rows.forEach { rowColors ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            )
            {
                rowColors.forEach { color ->
                    IconButton(
                        onClick = { viewModel.updateColor(color) },
                        modifier = Modifier
                            .size(30.dp)
                            .padding(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(color, shape = CircleShape)
                                .border( // Add border here
                                    width = 1.dp,
                                    color = if (selectedColor.value == color) Color.White else Color.Black, // Highlight selected
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(2.dp))
        HorizontalDivider(color = Black)
        // Description + save button
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
        )
        {
            BasicTextField(
                value = description.value,
                onValueChange = { viewModel.updateDescription(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 5.dp),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = Color.Black
                )
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){

                Spacer(Modifier.width(45.dp))

                IconButton(
                    onClick = {
                        if (speechState.isSpeaking) {
                            viewModel.voiceToTextParser.stopListening()
                        }
                        else {
                            viewModel.voiceToTextParser.startListening("en")
                        }
                    }
                ) {
                    AnimatedContent(
                        targetState = speechState.isSpeaking,
                        modifier = Modifier.size(width = 45.dp, height = 45.dp)
                    ) {isSpeaking ->
                        if (isSpeaking) {
                            Icon(painter = painterResource(R.drawable.baseline_mic_off_24), contentDescription = "Stop button")
                        } else {
                            Icon(painter = painterResource(R.drawable.baseline_mic_24), contentDescription = "Start button")
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .size(width = 45.dp, height = 45.dp) // Set the size of the button
                        .background(Color.Transparent, shape = RoundedCornerShape(8.dp)) // Rounded rectangle shape
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)) // Border
                        .clickable { showDialog = true }, // Toggle icon on click
                    contentAlignment = Alignment.Center // Center the icon
                )
                {
                    Icon(
                        painter = painterResource(
                            id = if (isSaved) R.drawable.ic_saved_256 else R.drawable.ic_save_256 // Toggle icon
                        ),
                        contentDescription = if (isSaved) "Saved" else "Save",
                        tint = Color.Black,
                        modifier = Modifier.size(35.dp) // Icon size
                    )
                }

            }

        }

    }


    if(showDialog){
        PickerDialog(
            onDismiss = { showDialog = false},
            onSave = { priority, category ->
                viewModel.updateSelectedPriority(priority)
                viewModel.updateSelectedCategory(category)
                viewModel.saveNote()
                showDialog = false
            },
            onAddCategory = { category ->
                viewModel.addCategory(category)
            },
            priorities = priorities,
            categories = categories
            )
    }



}