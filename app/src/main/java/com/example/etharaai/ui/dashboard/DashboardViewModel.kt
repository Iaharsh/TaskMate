package com.example.etharaai.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.etharaai.domain.repository.ProjectRepository
import com.example.etharaai.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _userContext = androidx.lifecycle.MutableLiveData<Pair<String, String>>()

    fun setUserContext(userId: String, role: String) {
        _userContext.value = userId to role
    }

    val todoCount = _userContext.switchMap { (userId, role) ->
        if (role == "Member") taskRepository.getCountByStatusForUser("Todo", userId).asLiveData()
        else taskRepository.getCountByStatus("Todo", userId).asLiveData()
    }

    val inProgressCount = _userContext.switchMap { (userId, role) ->
        if (role == "Member") taskRepository.getCountByStatusForUser("Doing", userId).asLiveData()
        else taskRepository.getCountByStatus("Doing", userId).asLiveData()
    }

    val completedCount = _userContext.switchMap { (userId, role) ->
        if (role == "Member") taskRepository.getCountByStatusForUser("Done", userId).asLiveData()
        else taskRepository.getCountByStatus("Done", userId).asLiveData()
    }

    val overdueCount = _userContext.switchMap { (userId, role) ->
        if (role == "Member") taskRepository.getOverdueCountForUser(System.currentTimeMillis(), userId).asLiveData()
        else taskRepository.getOverdueCount(System.currentTimeMillis(), userId).asLiveData()
    }
    
    val projectCount = _userContext.switchMap { (userId, role) ->
        if (role == "Member") projectRepository.getProjectsWithAssignedTasks(userId).map { it.size }.asLiveData()
        else projectRepository.getAllProjects(userId).map { it.size }.asLiveData()
    }

    val totalTasks = androidx.lifecycle.MediatorLiveData<Int>().apply {
        fun update() {
            val todo = todoCount.value ?: 0
            val doing = inProgressCount.value ?: 0
            val done = completedCount.value ?: 0
            value = todo + doing + done
        }
        addSource(todoCount) { update() }
        addSource(inProgressCount) { update() }
        addSource(completedCount) { update() }
    }
}
