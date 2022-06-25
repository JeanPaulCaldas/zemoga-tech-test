package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.Post

class DeletePost(private val repository: PostRepository) {
    suspend operator fun invoke(post:Post) = repository.deletePost(post)
}