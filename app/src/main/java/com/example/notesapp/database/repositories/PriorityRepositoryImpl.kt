package com.example.notesapp.database.repositories

import com.example.notesapp.database.daos.PriorityDao
import com.example.notesapp.domain.models.Priority
import com.example.notesapp.domain.repositories.PriorityRepository
import kotlinx.coroutines.flow.Flow

class PriorityRepositoryImpl (
    private val dao: PriorityDao
) : PriorityRepository {

    override fun getAllPriorities(): Flow<List<Priority>> {
        return dao.getAllPriorities()
    }

    override fun getPriorityById(id: Int): Priority? {
        return dao.getPriorityById(id)
    }

    override suspend fun upsertPriority(priority: Priority) {
        dao.upsertPriority(priority)
    }

    override suspend fun deletePriority(priority: Priority) {
        dao.deletePriority(priority)
    }
}