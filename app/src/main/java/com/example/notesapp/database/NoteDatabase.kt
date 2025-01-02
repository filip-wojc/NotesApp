package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesapp.database.daos.CategoryDao
import com.example.notesapp.database.daos.NoteDao
import com.example.notesapp.database.daos.PriorityDao
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.models.Note
import com.example.notesapp.domain.models.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [Note::class, Category::class, Priority::class],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val categoryDao: CategoryDao
    abstract val priorityDao: PriorityDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }

}