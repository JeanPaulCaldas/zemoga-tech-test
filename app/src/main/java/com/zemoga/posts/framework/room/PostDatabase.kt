package com.zemoga.posts.framework.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zemoga.posts.framework.room.daos.AuthorDao
import com.zemoga.posts.framework.room.daos.PostDao
import com.zemoga.posts.framework.room.entities.Author
import com.zemoga.posts.framework.room.entities.Post

@Database(entities = [Post::class, Author::class], version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun authorDao(): AuthorDao

    companion object {
        const val DATABASE_NAME: String = "post_db"
    }
}