package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.Post

class TogglePostFavorite(private val repository: PostRepository) {
    suspend operator fun invoke(post:Post):Post {
        repository.toggleFavoritePostStatus(post)
        return repository.getPost(post.id)
    }
}