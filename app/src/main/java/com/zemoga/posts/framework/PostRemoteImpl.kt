package com.zemoga.posts.framework

import com.zemoga.core.data.post.PostRemoteSource
import com.zemoga.core.domain.Post
import com.zemoga.posts.framework.server.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRemoteImpl(private val service: PostService) : PostRemoteSource {

    override suspend fun fetchAllPosts(): List<Post> =
        withContext(Dispatchers.IO) {
            service.getPosts().map { it.toDomain() }
        }

    override suspend fun fetchPostComments(postId: Int): List<String> =
        withContext(Dispatchers.IO) {
            service.getPostComments(postId).map { it.body }
        }
}