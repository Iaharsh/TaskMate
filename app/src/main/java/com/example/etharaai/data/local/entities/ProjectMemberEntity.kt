package com.example.etharaai.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "project_members",
    primaryKeys = ["projectId", "userId"],
    indices = [androidx.room.Index(value = ["userId"])]
)
data class ProjectMemberEntity(
    val projectId: String,
    val userId: String
)
