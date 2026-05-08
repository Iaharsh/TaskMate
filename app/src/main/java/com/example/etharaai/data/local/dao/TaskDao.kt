package com.example.etharaai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.etharaai.data.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    fun getTasksByProject(projectId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE assignedTo = :userId")
    fun getTasksByAssignee(userId: String): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: String, status: String)

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status")
    fun getCountByStatus(status: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE dueDate < :currentTime AND status != 'Done'")
    fun getOverdueCount(currentTime: Long): Flow<Int>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    @Query("UPDATE tasks SET assignedTo = :userId, assigneeName = :userName WHERE id = :taskId")
    suspend fun reassignTask(taskId: String, userId: String, userName: String)
}
