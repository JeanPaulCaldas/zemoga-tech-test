package com.zemoga.posts.framework.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Author(
    @PrimaryKey val id:Int,
    val name:String,
    val email:String,
    val phone:String,
    val website:String
)
