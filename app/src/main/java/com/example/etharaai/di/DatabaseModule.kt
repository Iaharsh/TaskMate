package com.example.etharaai.di

import android.content.Context
import androidx.room.Room
import com.example.etharaai.data.local.AppDatabase
import com.example.etharaai.data.local.dao.ProjectDao
import com.example.etharaai.data.local.dao.TaskDao
import com.example.etharaai.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ethara_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideProjectDao(db: AppDatabase): ProjectDao = db.projectDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()
}
