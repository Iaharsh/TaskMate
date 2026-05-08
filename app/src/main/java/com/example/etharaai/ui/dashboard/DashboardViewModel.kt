package com.example.etharaai.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

    val todoCount = taskRepository.getCountByStatus("Todo").asLiveData()
    val inProgressCount = taskRepository.getCountByStatus("Doing").asLiveData()
    val completedCount = taskRepository.getCountByStatus("Done").asLiveData()
    val overdueCount = taskRepository.getOverdueCount(System.currentTimeMillis()).asLiveData()
    
    val projectCount = projectRepository.getAllProjects().map { it.size }.asLiveData()

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
