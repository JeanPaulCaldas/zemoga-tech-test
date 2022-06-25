package com.zemoga.posts.di

import com.zemoga.core.data.author.AuthorCacheSource
import com.zemoga.core.data.author.AuthorRemoteSource
import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostCacheSource
import com.zemoga.core.data.post.PostRemoteSource
import com.zemoga.core.data.post.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun authorRepositoryProvider(
        cacheSource: AuthorCacheSource,
        remoteSource: AuthorRemoteSource
    ) = AuthorRepository(cacheSource, remoteSource)

    @Provides
    fun postRepositoryProvider(
        cacheSource: PostCacheSource,
        remoteSource: PostRemoteSource,
        authorRepository: AuthorRepository
    ) = PostRepository(cacheSource, remoteSource, authorRepository)
}