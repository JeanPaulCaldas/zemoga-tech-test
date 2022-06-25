package com.zemoga.core.data.author

class AuthorRepository(
    private val cacheSource: AuthorCacheSource,
    private val remoteSource: AuthorRemoteSource
) {

    suspend fun syncAuthors() {
        val authors = remoteSource.fetchAuthors()
        cacheSource.persistAuthors(authors)
    }

    suspend fun getAuthor(authorId: Int) = cacheSource.getAuthor(authorId)
}