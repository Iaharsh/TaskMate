package com.example.etharaai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.etharaai.data.local.dao.ProjectDao
import com.example.etharaai.data.local.dao.TaskDao
import com.example.etharaai.data.local.dao.UserDao
import com.example.etharaai.data.local.entities.ProjectEntity
import com.example.etharaai.data.local.entities.ProjectMemberEntity
import com.example.etharaai.data.local.entities.TaskEntity
import com.example.etharaai.data.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProjectEntity::class,
        TaskEntity::class,
        ProjectMemberEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
}
