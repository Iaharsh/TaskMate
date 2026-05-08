package com.example.etharaai.data.repository

import com.example.etharaai.data.local.dao.ProjectDao
import com.example.etharaai.data.local.entities.ProjectEntity
import com.example.etharaai.data.local.entities.ProjectMemberEntity
import com.example.etharaai.data.local.entities.UserEntity
import com.example.etharaai.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao
) : ProjectRepository {
    override suspend fun addProject(project: ProjectEntity) = projectDao.insertProject(project)
    override suspend fun addMemberToProject(projectId: String, userId: String) = 
        projectDao.insertProjectMember(ProjectMemberEntity(projectId, userId))
    override fun getAllProjects(ownerId: String): Flow<List<ProjectEntity>> = projectDao.getAllProjects(ownerId)
    override fun getProjectsWithMembers(ownerId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>> = projectDao.getProjectsWithMembers(ownerId)
    override fun getProjectsWithAssignedTasks(userId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>> = projectDao.getProjectsWithAssignedTasks(userId)
    override fun getProjectMembers(projectId: String): Flow<List<UserEntity>> = projectDao.getProjectMembers(projectId)
    override suspend fun deleteProject(projectId: String) = projectDao.deleteProject(projectId)
    override suspend fun removeMemberFromProject(projectId: String, userId: String) = projectDao.removeProjectMember(projectId, userId)
}
