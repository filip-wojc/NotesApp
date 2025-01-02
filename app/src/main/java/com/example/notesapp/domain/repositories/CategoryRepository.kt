package com.example.notesapp.domain.repositories

import com.example.notesapp.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories() : Flow<List<Category>>

    suspend fun getCategoryById(id: Int) : Category?

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(category: Category)
}