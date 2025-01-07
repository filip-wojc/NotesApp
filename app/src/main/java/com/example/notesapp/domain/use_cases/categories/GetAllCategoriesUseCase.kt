package com.example.notesapp.domain.use_cases.categories

import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.repositories.CategoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllCategoriesUseCase (
    private val repository: CategoryRepository
)  {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getAllCategories()
    }
}