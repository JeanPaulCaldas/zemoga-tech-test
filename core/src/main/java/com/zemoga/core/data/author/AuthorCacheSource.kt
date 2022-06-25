package com.zemoga.core.data.author

import com.zemoga.core.domain.Author

interface AuthorCacheSource {
    suspend fun getAuthor(authorId: Int): Author
    suspend fun persistAuthors(authors: List<Author>)
}