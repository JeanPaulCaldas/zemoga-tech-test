package com.zemoga.posts.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "author")
data class Author(
    @PrimaryKey val id:Int,
    val name:String,
    val email:String,
    val phone:String,
    val website:String
)
