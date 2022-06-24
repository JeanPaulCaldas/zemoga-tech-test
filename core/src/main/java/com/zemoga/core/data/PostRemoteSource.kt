package com.zemoga.core.data

import com.zemoga.core.domain.Post

interface PostRemoteSource {
    suspend fun fetchAllPosts(): List<Post>
}