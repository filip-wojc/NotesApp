package com.example.notesapp.domain.use_cases.categories

import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.repositories.CategoryRepository

class UpsertCategoryUseCase (
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.upsertCategory(category)
    }
}