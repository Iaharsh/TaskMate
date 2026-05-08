package com.example.etharaai.domain.repository

import com.example.etharaai.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: TaskEntity)
    fun getTasksByProject(projectId: String): Flow<List<TaskEntity>>
    fun getTasksByAssignee(userId: String): Flow<List<TaskEntity>>
    suspend fun updateTaskStatus(taskId: String, status: String)
    fun getCountByStatus(status: String): Flow<Int>
    fun getOverdueCount(currentTime: Long): Flow<Int>
    fun getAllTasks(): Flow<List<TaskEntity>>
    suspend fun deleteTask(taskId: String)
    suspend fun reassignTask(taskId: String, userId: String, userName: String)
}
