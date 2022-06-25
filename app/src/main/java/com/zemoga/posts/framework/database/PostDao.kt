package com.zemoga.posts.framework.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM Post ORDER BY favorite DESC, id")
    fun getAll(): Flow<List<Post>>

    @Query("SELECT * FROM Post WHERE favorite")
    fun getFavorites(): Flow<List<Post>>

    @Query("SELECT COUNT(id) FROM Post")
    suspend fun postCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(posts: List<Post>)

    @Query("SELECT * FROM Post WHERE id == :postId")
    suspend fun getPost(postId: Int): Post

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM Post")
    suspend fun deleteAllPosts()

    @Update
    suspend fun updatePost(post: Post)

}