package com.zemoga.posts.framework.database

import com.zemoga.core.data.PostCacheSource
import com.zemoga.core.domain.Post
import com.zemoga.posts.framework.toDomain
import com.zemoga.posts.framework.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomDataSource(private val postDao: PostDao) : PostCacheSource {

    override val allPosts: Flow<List<Post>>
        get() = postDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    override val favoritePosts: Flow<List<Post>>
        get() = TODO("Not yet implemented")

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) {
            postDao.postCount() == 0
        }

    override suspend fun persistPosts(posts: List<Post>) {
        withContext(Dispatchers.IO){
            postDao.insert(posts.map { it.toRoom() })
        }
    }

    override suspend fun deleteNoFavoritePosts() {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFavoriteStatus(post: Post) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(post: Post) {
        TODO("Not yet implemented")
    }
}