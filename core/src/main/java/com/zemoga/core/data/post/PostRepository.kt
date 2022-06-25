package com.zemoga.core.data.post

import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.domain.Post
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val cacheSource: PostCacheSource,
    private val postRemoteSource: PostRemoteSource,
    private val authorRepository: AuthorRepository
) {

    suspend fun getPosts(favorites: Boolean): Flow<List<Post>> {
        if (cacheSource.isEmpty()) syncPosts()
        return if (favorites) cacheSource.favoritePosts else cacheSource.allPosts
    }

    suspend fun getPost(postId: Int): Post =
        cacheSource.getPost(postId)

    suspend fun getPostComments(postId: Int): List<String> =
        postRemoteSource.fetchPostComments(postId)

    suspend fun syncPosts() {
        val posts = postRemoteSource.fetchAllPosts()
        cacheSource.persistPosts(posts)
        authorRepository.syncAuthors()
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

