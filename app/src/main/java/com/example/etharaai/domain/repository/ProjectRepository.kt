package com.example.etharaai.domain.repository

import com.example.etharaai.data.local.entities.ProjectEntity
import com.example.etharaai.data.local.entities.ProjectMemberEntity
import com.example.etharaai.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun addProject(project: ProjectEntity)
    suspend fun addMemberToProject(projectId: String, userId: String)
    fun getAllProjects(ownerId: String): Flow<List<ProjectEntity>>
    fun getProjectsWithMembers(ownerId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>>
    fun getProjectsWithAssignedTasks(userId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>>
    fun getProjectMembers(projectId: String): Flow<List<UserEntity>>
    suspend fun deleteProject(projectId: String)
    suspend fun removeMemberFromProject(projectId: String, userId: String)
}
