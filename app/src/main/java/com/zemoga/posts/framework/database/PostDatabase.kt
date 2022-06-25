package com.zemoga.posts.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Post::class, Author::class], version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun authorDao(): AuthorDao
}