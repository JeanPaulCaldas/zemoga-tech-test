package com.zemoga.posts.framework.room.daos

import androidx.room.*
import com.zemoga.posts.framework.room.entities.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post ORDER BY favorite DESC, id")
    fun getAll(): Flow<List<Post>>

    @Query("SELECT * FROM post WHERE favorite")
    fun getFavorites(): Flow<List<Post>>

    @Query("SELECT COUNT(id) FROM post")
    suspend fun postCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(posts: List<Post>)

    @Query("SELECT * FROM post WHERE id == :postId")
    suspend fun getPost(postId: Int): Post

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()

    @Update
    suspend fun updatePost(post: Post)

}