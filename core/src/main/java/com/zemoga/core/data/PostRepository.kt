package com.zemoga.core.data

import com.zemoga.core.domain.Post
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val cacheSource: PostCacheSource,
    private val remoteSource: PostRemoteSource
) {

    suspend fun getPosts(favorites: Boolean): Flow<List<Post>> {
        if (cacheSource.isEmpty()) fetchPosts()
        return if (favorites) cacheSource.favoritePosts else cacheSource.allPosts
    }

    suspend fun fetchPosts() {
        val posts = remoteSource.fetchAllPosts()
        cacheSource.persistPosts(posts)
    }

    suspend fun deleteCachedPosts() {
        cacheSource.deleteNoFavoritePosts()
    }

    suspend fun toggleFavoritePostStatus(post: Post) {
        cacheSource.toggleFavoriteStatus(post)

    }

    suspend fun deletePost(post: Post) {
        cacheSource.deletePost(post)
    }
}

