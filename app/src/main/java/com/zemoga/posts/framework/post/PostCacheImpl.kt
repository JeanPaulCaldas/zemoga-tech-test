package com.zemoga.posts.framework.post

import com.zemoga.core.data.post.PostCacheSource
import com.zemoga.core.domain.Post
import com.zemoga.posts.framework.room.daos.PostDao
import com.zemoga.posts.framework.util.toDomain
import com.zemoga.posts.framework.util.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PostCacheImpl(private val postDao: PostDao) : PostCacheSource {

    override val allPosts: Flow<List<Post>>
        get() = postDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override val favoritePosts: Flow<List<Post>>
        get() = postDao.getFavorites().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getPost(postId: Int): Post =
        withContext(Dispatchers.IO) {
            postDao.getPost(postId).toDomain()
        }

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) {
            postDao.postCount() == 0
        }

    override suspend fun persistPosts(posts: List<Post>) {
        withContext(Dispatchers.IO) {
            postDao.insert(posts.map { it.toRoom() })
        }
    }

    override suspend fun deleteAllPosts() {
        withContext(Dispatchers.IO) {
            postDao.deleteAllPosts()
        }
    }

    override suspend fun updatePost(post: Post) {
        withContext(Dispatchers.IO) {
            postDao.updatePost(post.toRoom())
        }
    }

    override suspend fun deletePost(post: Post) {
        withContext(Dispatchers.IO) {
            postDao.deletePost(post.toRoom())
        }
    }
}