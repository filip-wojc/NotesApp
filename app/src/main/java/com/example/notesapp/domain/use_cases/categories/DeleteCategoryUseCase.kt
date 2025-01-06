package com.example.notesapp.domain.use_cases.categories

import androidx.core.os.requestProfiling
import com.example.notesapp.domain.models.Category
import com.example.notesapp.domain.repositories.CategoryRepository

class DeleteCategoryUseCase (
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.deleteCategory(category)
    }
}