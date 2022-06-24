package com.zemoga.posts.framework

import com.zemoga.core.data.PostRemoteSource
import com.zemoga.core.domain.Post
import com.zemoga.posts.framework.server.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JsonPlaceHolderDataSource(private val postService: PostService) : PostRemoteSource {

    override suspend fun fetchAllPosts(): List<Post> =
        withContext(Dispatchers.IO) {
            postService.getPosts().map { it.toDomain() }
        }
}