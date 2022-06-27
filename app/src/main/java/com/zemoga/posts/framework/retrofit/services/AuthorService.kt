package com.zemoga.posts.framework.retrofit.services

import com.zemoga.posts.framework.retrofit.entities.Author
import retrofit2.http.GET

interface AuthorService {
    @GET("users")
    suspend fun getAuthors(): List<Author>
}