package com.zemoga.core.data.post

import com.zemoga.core.domain.Post

interface PostRemoteSource {
    suspend fun fetchAllPosts(): List<Post>
    suspend fun fetchPostComments(postId: Int): List<String>
}