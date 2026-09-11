package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PreparednessTaskDao {

    @Query("SELECT * FROM preparedness_tasks ORDER BY sortOrder ASC")
    fun getAllTasks(): Flow<List<PreparednessTaskEntity>>

    @Query("SELECT * FROM preparedness_tasks WHERE category = :category ORDER BY sortOrder ASC")
    fun getTasksByCategory(category: String): Flow<List<PreparednessTaskEntity>>

    @Query("SELECT COUNT(*) FROM preparedness_tasks WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM preparedness_tasks")
    suspend fun getTotalCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<PreparednessTaskEntity>)

    @Query("UPDATE preparedness_tasks SET isCompleted = :isCompleted, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTaskCompletion(id: String, isCompleted: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE preparedness_tasks SET isCompleted = :isCompleted, updatedAt = :updatedAt")
    suspend fun setAllCompleted(isCompleted: Boolean, updatedAt: Long = System.currentTimeMillis())
}
