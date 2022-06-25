package com.zemoga.posts.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM Post")
    fun getAll(): Flow<List<Post>>

    @Query("SELECT * FROM Post WHERE favorite")
    fun getFavorites(): Flow<List<Post>>

    @Query("SELECT COUNT(id) FROM Post")
    suspend fun postCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(posts: List<Post>)

    @Query("SELECT * FROM Post WHERE id == :postId")
    suspend fun getPost(postId: Int): Post

}