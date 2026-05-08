package com.example.etharaai.domain.repository

import com.example.etharaai.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun addTask(task: TaskEntity)
    fun getTasksByProject(projectId: String, ownerId: String): Flow<List<TaskEntity>>
    fun getTasksByAssignee(userId: String): Flow<List<TaskEntity>>
    suspend fun updateTaskStatus(taskId: String, status: String)
    fun getCountByStatus(status: String, ownerId: String): Flow<Int>
    fun getCountByStatusForUser(status: String, userId: String): Flow<Int>
    fun getOverdueCount(currentTime: Long, ownerId: String): Flow<Int>
    fun getOverdueCountForUser(currentTime: Long, userId: String): Flow<Int>
    fun getAllTasks(ownerId: String): Flow<List<TaskEntity>>
    suspend fun deleteTask(taskId: String)
    suspend fun reassignTask(taskId: String, userId: String?, userName: String?)
}
