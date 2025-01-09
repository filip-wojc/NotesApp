package com.example.notesapp.domain.use_cases.priorities

import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.PriorityRepository

class UpsertPriorityUseCase(private val repository: PriorityRepository) {
    suspend operator fun invoke(priority: Priority){
        repository.upsertPriority(priority)
    }
}