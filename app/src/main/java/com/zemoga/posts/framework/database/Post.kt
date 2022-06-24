package com.zemoga.posts.framework.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey val id: Int,
    val authorId: Int,
    val title: String,
    val description: String,
    val favorite: Boolean
)
