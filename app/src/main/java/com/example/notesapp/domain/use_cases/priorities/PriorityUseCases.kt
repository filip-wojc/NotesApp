package com.example.notesapp.domain.use_cases.priorities

data class PriorityUseCases(
    val getAllPriorities: GetAllPrioritiesUseCase,
    val upsertPriority: UpsertPriorityUseCase,
    val deletePriority: DeletePriorityUseCase
    )
