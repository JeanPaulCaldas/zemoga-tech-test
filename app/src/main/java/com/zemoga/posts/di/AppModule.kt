package com.zemoga.posts.di

import android.app.Application
import androidx.room.Room
import com.zemoga.core.data.PostCacheSource
import com.zemoga.posts.framework.database.PostDatabase
import com.zemoga.posts.framework.database.RoomDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application) =
        Room.databaseBuilder(app, PostDatabase::class.java, "post-db").build()

    @Provides
    fun cacheDataSourceProvider(db: PostDatabase): PostCacheSource = RoomDataSource(db.postDao())
}