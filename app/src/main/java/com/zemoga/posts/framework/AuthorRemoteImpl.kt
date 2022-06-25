package com.zemoga.posts.framework

import com.zemoga.core.data.author.AuthorRemoteSource
import com.zemoga.core.domain.Author
import com.zemoga.posts.framework.server.AuthorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorRemoteImpl(private val service: AuthorService) : AuthorRemoteSource {
    override suspend fun fetchAuthors(): List<Author> =
        withContext(Dispatchers.IO) {
            service.getAuthors().map { it.toDomain() }
        }

}