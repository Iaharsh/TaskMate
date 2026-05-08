package com.example.etharaai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val assignedTo: String?,
    val assigneeName: String?,
    val status: String, // Todo, Doing, Done
    val dueDate: Long,
    val priority: String, // Low, Medium, High
    val ownerId: String
)
