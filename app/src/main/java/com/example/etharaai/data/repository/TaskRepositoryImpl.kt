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
    override fun getTasksByProject(projectId: String, ownerId: String) = taskDao.getTasksByProject(projectId, ownerId)
    override fun getTasksByAssignee(userId: String) = taskDao.getTasksByAssignee(userId)
    override suspend fun updateTaskStatus(taskId: String, status: String) = taskDao.updateTaskStatus(taskId, status)
    override fun getCountByStatus(status: String, ownerId: String) = taskDao.getCountByStatus(status, ownerId)
    override fun getCountByStatusForUser(status: String, userId: String) = taskDao.getCountByStatusForUser(status, userId)
    override fun getOverdueCount(currentTime: Long, ownerId: String) = taskDao.getOverdueCount(currentTime, ownerId)
    override fun getOverdueCountForUser(currentTime: Long, userId: String) = taskDao.getOverdueCountForUser(currentTime, userId)
    override fun getAllTasks(ownerId: String): Flow<List<TaskEntity>> = taskDao.getAllTasks(ownerId)
    override suspend fun deleteTask(taskId: String) = taskDao.deleteTask(taskId)
    override suspend fun reassignTask(taskId: String, userId: String?, userName: String?) = 
        taskDao.reassignTask(taskId, userId, userName)
}
