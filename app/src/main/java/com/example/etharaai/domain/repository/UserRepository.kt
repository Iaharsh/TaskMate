package com.example.etharaai.domain.repository

import com.example.etharaai.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: UserEntity)
    suspend fun getUser(userId: String): UserEntity?
    suspend fun getUserByEmail(email: String): UserEntity?
    fun getAllUsers(): Flow<List<UserEntity>>
}
