package com.example.notesapp.domain.use_cases.categories

import com.example.notesapp.domain.repositories.CategoryRepository

class GetAllCategoriesUseCase (
    private val repository: CategoryRepository
) {
    operator fun invoke() {
        repository.getAllCategories()
    }
}