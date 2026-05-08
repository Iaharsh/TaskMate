package com.example.etharaai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.etharaai.data.local.entities.ProjectEntity
import com.example.etharaai.data.local.entities.ProjectMemberEntity
import com.example.etharaai.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectMember(member: ProjectMemberEntity)

    @Query("SELECT * FROM projects WHERE ownerId = :ownerId")
    fun getAllProjects(ownerId: String): Flow<List<ProjectEntity>>

    @androidx.room.Transaction
    @Query("SELECT * FROM projects WHERE ownerId = :ownerId")
    fun getProjectsWithMembers(ownerId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>>

    @Query("""
        SELECT users.* FROM users 
        INNER JOIN project_members ON users.id = project_members.userId 
        WHERE project_members.projectId = :projectId
    """)
    fun getProjectMembers(projectId: String): Flow<List<UserEntity>>

    @androidx.room.Transaction
    @Query("""
        SELECT DISTINCT projects.* FROM projects 
        INNER JOIN tasks ON projects.id = tasks.projectId 
        WHERE tasks.assignedTo = :userId
    """)
    fun getProjectsWithAssignedTasks(userId: String): Flow<List<com.example.etharaai.data.local.entities.ProjectWithMembers>>

    @Query("DELETE FROM projects WHERE id = :projectId")
    suspend fun deleteProject(projectId: String)

    @Query("DELETE FROM project_members WHERE projectId = :projectId AND userId = :userId")
    suspend fun removeProjectMember(projectId: String, userId: String)
}
