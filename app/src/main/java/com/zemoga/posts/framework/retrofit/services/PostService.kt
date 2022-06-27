package com.zemoga.posts.framework.retrofit.services

import com.zemoga.posts.framework.retrofit.entities.Comment
import com.zemoga.posts.framework.retrofit.entities.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface PostService {

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{id}/comments")
    suspend fun getPostComments(@Path("id") postId: Int): List<Comment>
}