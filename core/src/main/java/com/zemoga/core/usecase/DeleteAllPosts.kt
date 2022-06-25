package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository

class DeleteAllPosts(private val repository: PostRepository) {
    suspend operator fun invoke() = resultOf {
        repository.deleteCachedPosts()
    }
}