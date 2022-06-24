package com.zemoga.posts.framework.server

import retrofit2.http.GET

interface PostService {

    @GET("posts")
    suspend fun getPosts(): List<Post>
}