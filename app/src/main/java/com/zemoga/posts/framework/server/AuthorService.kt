package com.zemoga.posts.framework.server

import retrofit2.http.GET

interface AuthorService {
    @GET("users")
    suspend fun getAuthors(): List<Author>
}