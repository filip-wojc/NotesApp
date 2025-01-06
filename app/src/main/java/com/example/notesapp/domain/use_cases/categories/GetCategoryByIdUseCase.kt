package com.example.notesapp.domain.use_cases.categories

import com.example.notesapp.domain.repositories.CategoryRepository

class GetCategoryByIdUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.getCategoryById(id)
    }
}