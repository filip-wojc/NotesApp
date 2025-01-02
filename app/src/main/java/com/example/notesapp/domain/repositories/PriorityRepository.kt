package com.example.notesapp.domain.repositories

import com.example.notesapp.domain.models.Priority
import kotlinx.coroutines.flow.Flow

interface PriorityRepository {
    fun getAllPriorities() : Flow<List<Priority>>

    fun getPriorityById(id: Int) : Priority?

    suspend fun upsertPriority(priority: Priority)

    suspend fun deletePriority(priority: Priority)
}