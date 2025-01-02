package com.example.notesapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notesapp.domain.models.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("Select * From category")
    fun getAllCategories() : Flow<List<Category>>

    @Query("Select * From Category Where categoryId=:id")
    suspend fun getCategoryById(id: Int) : Category?

    @Upsert
    suspend fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}