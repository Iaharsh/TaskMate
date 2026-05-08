package com.example.etharaai.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.etharaai.data.local.entities.ProjectEntity
import com.example.etharaai.domain.repository.ProjectRepository
import com.example.etharaai.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userContext = androidx.lifecycle.MutableLiveData<Pair<String, String>>()

    fun setUserContext(userId: String, role: String) {
        _userContext.value = userId to role
    }

    val projects = _userContext.switchMap { (userId, role) ->
        if (role == "Member") projectRepository.getProjectsWithAssignedTasks(userId).asLiveData()
        else projectRepository.getProjectsWithMembers(userId).asLiveData()
    }

    fun createProject(name: String, description: String) {
        viewModelScope.launch {
            val project = ProjectEntity(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                ownerId = _userContext.value?.first ?: "admin"
            )
            projectRepository.addProject(project)
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            projectRepository.deleteProject(projectId)
        }
    }

    fun addMemberByEmail(projectId: String, email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null) {
                projectRepository.addMemberToProject(projectId, user.id)
                onResult(true, "Member added successfully")
            } else {
                onResult(false, "User not found")
            }
        }
    }

    fun removeMember(projectId: String, userId: String) {
        viewModelScope.launch {
            projectRepository.removeMemberFromProject(projectId, userId)
        }
    }
}
