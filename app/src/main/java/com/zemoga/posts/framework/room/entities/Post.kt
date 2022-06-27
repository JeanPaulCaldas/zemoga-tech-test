package com.zemoga.posts.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(
    @PrimaryKey val id: Int,
    val authorId: Int,
    val title: String,
    val description: String,
    val favorite: Boolean
)
