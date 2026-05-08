package com.example.etharaai.data.local.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ProjectWithMembers(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProjectMemberEntity::class,
            parentColumn = "projectId",
            entityColumn = "userId"
        )
    )
    val members: List<UserEntity>
)
