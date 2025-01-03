package com.example.notesapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.repositories.CategoryRepositoryImpl
import com.example.notesapp.database.repositories.NoteRepositoryImpl
import com.example.notesapp.database.repositories.PriorityRepositoryImpl
import com.example.notesapp.domain.repositories.CategoryRepository
import com.example.notesapp.domain.repositories.NoteRepository
import com.example.notesapp.domain.repositories.PriorityRepository
import com.example.notesapp.domain.use_cases.notes.DeleteNoteUseCase
import com.example.notesapp.domain.use_cases.notes.GetAllNotesUseCase
import com.example.notesapp.domain.use_cases.notes.GetNoteByIdUseCase
import com.example.notesapp.domain.use_cases.notes.GetNotesByCategoryIdUseCase
import com.example.notesapp.domain.use_cases.notes.GetNotesByPriorityIdUseCase
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import com.example.notesapp.domain.use_cases.notes.UpsertNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext app: Context
        // app: Context - Potem się zmieni, jak nie będzie
        // potrzeby wykorzystywać main activity
    ): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: NoteDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun providePriorityRepository(db: NoteDatabase): PriorityRepository {
        return PriorityRepositoryImpl(db.priorityDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotes = GetAllNotesUseCase(repository),
            getNotesByCategory = GetNotesByCategoryIdUseCase(repository),
            getNotesByPriority = GetNotesByPriorityIdUseCase(repository),
            getNote = GetNoteByIdUseCase(repository),
            upsertNote = UpsertNoteUseCase(repository),
            deleteNotes = DeleteNoteUseCase(repository),
        )
    }

}