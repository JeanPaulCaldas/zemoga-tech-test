package com.zemoga.core.usecase

import com.zemoga.core.data.PostRepository

class GetPosts (private val repository: PostRepository) {
    suspend operator fun invoke(favorites: Boolean) = repository.getPosts(favorites)
}