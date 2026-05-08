package com.example.etharaai.data.repository

import com.example.etharaai.data.local.dao.TaskDao
import com.example.etharaai.data.local.entities.TaskEntity
import com.example.etharaai.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun addTask(task: TaskEntity) = taskDao.insertTask(task)
    override fun getTasksByProject(projectId: String) = taskDao.getTasksByProject(projectId)
    override fun getTasksByAssignee(userId: String) = taskDao.getTasksByAssignee(userId)
    override suspend fun updateTaskStatus(taskId: String, status: String) = taskDao.updateTaskStatus(taskId, status)
    override fun getCountByStatus(status: String) = taskDao.getCountByStatus(status)
    override fun getOverdueCount(currentTime: Long) = taskDao.getOverdueCount(currentTime)
    override fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    override suspend fun deleteTask(taskId: String) = taskDao.deleteTask(taskId)
    override suspend fun reassignTask(taskId: String, userId: String, userName: String) = 
        taskDao.reassignTask(taskId, userId, userName)
}
