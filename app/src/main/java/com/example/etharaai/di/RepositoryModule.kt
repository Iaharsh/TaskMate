package com.example.etharaai.di

import com.example.etharaai.data.repository.ProjectRepositoryImpl
import com.example.etharaai.data.repository.TaskRepositoryImpl
import com.example.etharaai.data.repository.UserRepositoryImpl
import com.example.etharaai.domain.repository.ProjectRepository
import com.example.etharaai.domain.repository.TaskRepository
import com.example.etharaai.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindProjectRepository(impl: ProjectRepositoryImpl): ProjectRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}
