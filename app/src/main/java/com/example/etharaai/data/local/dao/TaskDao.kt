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

    @Query("SELECT * FROM tasks WHERE projectId = :projectId AND ownerId = :ownerId")
    fun getTasksByProject(projectId: String, ownerId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE assignedTo = :userId")
    fun getTasksByAssignee(userId: String): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: String, status: String)

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status AND ownerId = :ownerId")
    fun getCountByStatus(status: String, ownerId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status AND assignedTo = :userId")
    fun getCountByStatusForUser(status: String, userId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE dueDate < :currentTime AND status != 'Done' AND ownerId = :ownerId")
    fun getOverdueCount(currentTime: Long, ownerId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE dueDate < :currentTime AND status != 'Done' AND assignedTo = :userId")
    fun getOverdueCountForUser(currentTime: Long, userId: String): Flow<Int>

    @Query("SELECT * FROM tasks WHERE ownerId = :ownerId")
    fun getAllTasks(ownerId: String): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: String)

    @Query("UPDATE tasks SET assignedTo = :userId, assigneeName = :userName WHERE id = :taskId")
    suspend fun reassignTask(taskId: String, userId: String?, userName: String?)
}
