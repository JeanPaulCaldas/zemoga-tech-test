package com.zemoga.core.data.author

import com.zemoga.core.domain.Author

interface AuthorRemoteSource {
    suspend fun fetchAuthors(): List<Author>
}