package com.example.etharaai.data.repository

import com.example.etharaai.data.local.dao.UserDao
import com.example.etharaai.data.local.entities.UserEntity
import com.example.etharaai.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)
    override suspend fun getUser(userId: String) = null // Not needed for now, or implement correctly
    override suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    override fun getAllUsers() = userDao.getAllUsers()
}
