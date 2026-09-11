package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preparedness_tasks")
data class PreparednessTaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: String, // "GO_BAG", "UTILITIES", "EVACUATION", "MEDICAL", "DOCUMENTS"
    val categoryLabel: String,
    val isCompleted: Boolean = false,
    val priority: String = "HIGH", // "CRITICAL", "HIGH", "RECOMMENDED"
    val sortOrder: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
