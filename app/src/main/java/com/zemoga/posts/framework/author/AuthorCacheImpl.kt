package com.zemoga.posts.framework.author

import com.zemoga.core.data.author.AuthorCacheSource
import com.zemoga.core.domain.Author
import com.zemoga.posts.framework.room.daos.AuthorDao
import com.zemoga.posts.framework.util.toDomain
import com.zemoga.posts.framework.util.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorCacheImpl(private val authorDao: AuthorDao) : AuthorCacheSource {

    override suspend fun getAuthor(authorId: Int): Author =
        withContext(Dispatchers.IO) {
            authorDao.getAuthor(authorId).toDomain()
        }

    override suspend fun persistAuthors(authors: List<Author>) {
        withContext(Dispatchers.IO) {
            authorDao.insert(authors.map { it.toRoom() })
        }
    }
}