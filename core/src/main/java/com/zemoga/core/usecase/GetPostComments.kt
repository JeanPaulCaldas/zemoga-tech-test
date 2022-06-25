package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository

class GetPostComments(private val repository: PostRepository) {
    suspend operator fun invoke(postId: Int) = resultOf {
        repository.getPostComments(postId)
    }
}