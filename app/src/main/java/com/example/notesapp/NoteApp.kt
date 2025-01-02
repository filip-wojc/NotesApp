package com.example.notesapp

import android.app.Application
import com.example.notesapp.database.NoteDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApp : Application()