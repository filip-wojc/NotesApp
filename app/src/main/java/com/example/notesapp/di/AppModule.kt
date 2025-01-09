package com.example.notesapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.repositories.CategoryRepositoryImpl
import com.example.notesapp.database.repositories.NoteRepositoryImpl
import com.example.notesapp.database.repositories.PriorityRepositoryImpl
import com.example.notesapp.domain.repositories.CategoryRepository
import com.example.notesapp.domain.repositories.NoteRepository
import com.example.notesapp.domain.repositories.PriorityRepository
import com.example.notesapp.domain.use_cases.categories.CategoryUseCases
import com.example.notesapp.domain.use_cases.categories.DeleteCategoryUseCase
import com.example.notesapp.domain.use_cases.categories.GetAllCategoriesUseCase
import com.example.notesapp.domain.use_cases.categories.GetCategoryByIdUseCase
import com.example.notesapp.domain.use_cases.categories.UpsertCategoryUseCase
import com.example.notesapp.domain.use_cases.notes.DeleteNoteUseCase
import com.example.notesapp.domain.use_cases.notes.GetAllNotesUseCase
import com.example.notesapp.domain.use_cases.notes.GetNoteByIdUseCase
import com.example.notesapp.domain.use_cases.notes.GetNotesByCategoryIdUseCase
import com.example.notesapp.domain.use_cases.notes.NoteUseCases
import com.example.notesapp.domain.use_cases.notes.UpsertNoteUseCase
import com.example.notesapp.domain.use_cases.priorities.DeletePriorityUseCase
import com.example.notesapp.domain.use_cases.priorities.GetAllPrioritiesUseCase
import com.example.notesapp.domain.use_cases.priorities.PriorityUseCases
import com.example.notesapp.domain.use_cases.priorities.UpsertPriorityUseCase
import com.example.notesapp.domain.utils.VoiceToTextParser
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
            getNote = GetNoteByIdUseCase(repository),
            upsertNote = UpsertNoteUseCase(repository),
            deleteNotes = DeleteNoteUseCase(repository),
        )
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository) : CategoryUseCases {
        return CategoryUseCases(
            getAllCategories = GetAllCategoriesUseCase(repository),
            getCategoryById = GetCategoryByIdUseCase(repository),
            upsertCategory = UpsertCategoryUseCase(repository),
            deleteCategory = DeleteCategoryUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providePriorityUseCases(repository: PriorityRepository): PriorityUseCases{
        return PriorityUseCases(
            getAllPriorities = GetAllPrioritiesUseCase(repository),
            upsertPriority = UpsertPriorityUseCase(repository),
            deletePriority = DeletePriorityUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideVoiceParser(
        @ApplicationContext app: Context
    ) : VoiceToTextParser{
        return VoiceToTextParser(app)
    }

}