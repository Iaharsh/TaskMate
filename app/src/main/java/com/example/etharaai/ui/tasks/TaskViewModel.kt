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
    private val projectRepository: com.example.etharaai.domain.repository.ProjectRepository,
    private val userRepository: com.example.etharaai.domain.repository.UserRepository
) : ViewModel() {

    private val _projectId = androidx.lifecycle.MutableLiveData<String?>(null)
    private val _userRole = androidx.lifecycle.MutableLiveData<String>("Member")
    private val _userId = androidx.lifecycle.MutableLiveData<String>("")

    private val combinedTrigger = androidx.lifecycle.MediatorLiveData<Triple<String?, String?, String?>>().apply {
        addSource(_projectId) { value = Triple(it, _userRole.value, _userId.value) }
        addSource(_userRole) { value = Triple(_projectId.value, it, _userId.value) }
        addSource(_userId) { value = Triple(_projectId.value, _userRole.value, it) }
    }

    val tasks: androidx.lifecycle.LiveData<List<TaskEntity>> = combinedTrigger.switchMap { (projectId, role, userId) ->
        val currentUserId = userId ?: ""
        val baseTasks = if (projectId.isNullOrEmpty()) {
            taskRepository.getAllTasks(currentUserId).asLiveData()
        } else {
            taskRepository.getTasksByProject(projectId, currentUserId).asLiveData()
        }

        baseTasks.switchMap { taskList ->
            val filteredTasks = if (role == "Member") {
                taskList.filter { it.assignedTo == currentUserId }
            } else {
                taskList // Already filtered by ownerId in baseTasks
            }
            androidx.lifecycle.MutableLiveData(filteredTasks)
        }
    }

    fun setUserContext(userId: String, userRole: String) {
        _userId.value = userId
        _userRole.value = userRole
    }

    fun getProjectMembers(projectId: String) = projectRepository.getProjectMembers(projectId).asLiveData()

    fun getAllUsers() = userRepository.getAllUsers().asLiveData()

    fun setProjectId(projectId: String?) {
        _projectId.value = if (projectId.isNullOrEmpty()) null else projectId
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
                dueDate = System.currentTimeMillis(),
                ownerId = _userId.value ?: "admin"
            )
            taskRepository.addTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.addTask(task) // addTask uses OnConflictStrategy.REPLACE
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

    fun reassignTask(taskId: String, userId: String?, userName: String?) {
        viewModelScope.launch {
            taskRepository.reassignTask(taskId, userId, userName)
        }
    }
}
