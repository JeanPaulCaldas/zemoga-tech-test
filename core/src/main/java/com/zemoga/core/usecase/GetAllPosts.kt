package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository

class GetAllPosts (private val repository: PostRepository) {
    suspend operator fun invoke(favorites: Boolean) = repository.getPosts(favorites)
}