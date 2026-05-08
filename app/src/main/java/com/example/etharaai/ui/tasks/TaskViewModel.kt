package com.example.etharaai.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.etharaai.data.local.entities.TaskEntity
import com.example.etharaai.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.switchMap
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: com.example.etharaai.domain.repository.ProjectRepository
) : ViewModel() {

    private val _projectId = androidx.lifecycle.MutableLiveData<String?>()
    val tasks: androidx.lifecycle.LiveData<List<TaskEntity>> = _projectId.switchMap { projectId ->
        if (projectId == null) {
            taskRepository.getAllTasks().asLiveData()
        } else {
            taskRepository.getTasksByProject(projectId).asLiveData()
        }
    }

    fun getProjectMembers(projectId: String) = projectRepository.getProjectMembers(projectId).asLiveData()

    fun setProjectId(projectId: String?) {
        _projectId.value = projectId
    }

    fun createTask(
        title: String, 
        description: String, 
        priority: String, 
        status: String, 
        assignedTo: String?, 
        assigneeName: String?
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                id = UUID.randomUUID().toString(),
                projectId = _projectId.value ?: "general",
                title = title,
                description = description,
                assignedTo = assignedTo,
                assigneeName = assigneeName,
                status = status,
                priority = priority,
                dueDate = System.currentTimeMillis()
            )
            taskRepository.addTask(task)
        }
    }

    fun updateTaskStatus(taskId: String, status: String) {
        viewModelScope.launch {
            taskRepository.updateTaskStatus(taskId, status)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    fun reassignTask(taskId: String, userId: String, userName: String) {
        viewModelScope.launch {
            taskRepository.reassignTask(taskId, userId, userName)
        }
    }
}
