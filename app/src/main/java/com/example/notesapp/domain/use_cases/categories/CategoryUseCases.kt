package com.example.notesapp.domain.use_cases.categories

data class CategoryUseCases (
    val getAllCategories: GetAllCategoriesUseCase,
    val getCategoryById: GetCategoryByIdUseCase,
    val upsertCategory: UpsertCategoryUseCase,
    val deleteCategory: DeleteCategoryUseCase
)