package com.zemoga.posts.framework

import com.zemoga.core.data.author.AuthorCacheSource
import com.zemoga.core.domain.Author
import com.zemoga.posts.framework.database.AuthorDao

class AuthorCacheImpl(private val authorDao: AuthorDao) : AuthorCacheSource {

    override suspend fun getAuthor(authorId: Int): Author =
        authorDao.getAuthor(authorId).toDomain()

    override suspend fun persistAuthors(authors: List<Author>) {
        authorDao.insert(authors.map { it.toRoom() })
    }
}