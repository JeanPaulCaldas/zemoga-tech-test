package com.zemoga.posts.framework

import com.zemoga.core.data.author.AuthorRemoteSource
import com.zemoga.core.domain.Author
import com.zemoga.posts.framework.server.AuthorService

class AuthorRemoteImpl(private val service: AuthorService) : AuthorRemoteSource {
    override suspend fun fetchAuthors(): List<Author> =
        service.getAuthors().map { it.toDomain() }

}