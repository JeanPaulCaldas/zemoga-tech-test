package com.zemoga.posts.di

import com.zemoga.core.data.PostCacheSource
import com.zemoga.core.data.PostRemoteSource
import com.zemoga.core.data.PostRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun postRepositoryProvider(
        cacheSource: PostCacheSource,
        remoteSource: PostRemoteSource
    ) = PostRepository(cacheSource, remoteSource)
}