package com.zemoga.posts.di

import com.zemoga.core.data.PostRemoteSource
import com.zemoga.posts.framework.JsonPlaceHolderDataSource
import com.zemoga.posts.framework.server.PostService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() = OkHttpClient.Builder().build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit) = retrofit.create(PostService::class.java)

    @Provides
    fun remoteDataSourceProvider(postService: PostService): PostRemoteSource =
        JsonPlaceHolderDataSource(postService)
}