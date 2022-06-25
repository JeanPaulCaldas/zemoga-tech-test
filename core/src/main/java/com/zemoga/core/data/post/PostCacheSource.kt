package com.zemoga.core.data.post

import com.zemoga.core.domain.Post
import kotlinx.coroutines.flow.Flow

interface PostCacheSource {
    val allPosts: Flow<List<Post>>
    val favoritePosts: Flow<List<Post>>
    suspend fun getPost(postId: Int): Post
    suspend fun isEmpty(): Boolean
    suspend fun persistPosts(posts: List<Post>)
    suspend fun deleteNoFavoritePosts()
    suspend fun toggleFavoriteStatus(post: Post)
    suspend fun deletePost(post: Post)
}