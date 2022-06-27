package com.zemoga.posts.di

import android.app.Application
import androidx.room.Room
import com.zemoga.core.data.author.AuthorCacheSource
import com.zemoga.core.data.post.PostCacheSource
import com.zemoga.posts.framework.author.AuthorCacheImpl
import com.zemoga.posts.framework.room.PostDatabase
import com.zemoga.posts.framework.post.PostCacheImpl
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
        Room.databaseBuilder(app, PostDatabase::class.java, PostDatabase.DATABASE_NAME).build()

    @Provides
    fun postCacheProvider(db: PostDatabase): PostCacheSource = PostCacheImpl(db.postDao())

    @Provides
    fun authorCacheProvider(db: PostDatabase): AuthorCacheSource = AuthorCacheImpl(db.authorDao())
}