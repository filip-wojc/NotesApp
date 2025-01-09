package com.example.notesapp.domain.use_cases.priorities

import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.PriorityRepository
import kotlinx.coroutines.flow.Flow

class GetAllPrioritiesUseCase(private val repository: PriorityRepository ) {
    operator fun invoke(): Flow<List<Priority>> {
        return repository.getAllPriorities()
    }

}