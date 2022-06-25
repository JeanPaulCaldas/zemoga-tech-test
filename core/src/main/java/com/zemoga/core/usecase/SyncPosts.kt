package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository

class SyncPosts(private val repository: PostRepository) {
    suspend operator fun invoke() = repository.syncPosts()
}