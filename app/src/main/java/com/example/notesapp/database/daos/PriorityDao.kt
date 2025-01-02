package com.example.notesapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notesapp.domain.models.Priority
import kotlinx.coroutines.flow.Flow

@Dao
interface PriorityDao {
    @Query("Select * From Priority")
    fun getAllPriorities() : Flow<List<Priority>>

    @Query("Select * From Priority Where priorityId=:id")
    fun getPriorityById(id: Int) : Priority?

    @Upsert
    suspend fun upsertPriority(priority: Priority)

    @Delete
    suspend fun deletePriority(priority: Priority)
}