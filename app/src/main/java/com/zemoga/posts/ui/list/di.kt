package com.zemoga.posts.ui.list

import androidx.lifecycle.SavedStateHandle
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.usecase.DeleteAllPosts
import com.zemoga.core.usecase.GetAllPosts
import com.zemoga.core.usecase.SyncPosts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class ListFragmentModule {

    @Provides
    @Named("favorite")
    fun favoritesProvider(stateHandle: SavedStateHandle): Boolean {
        return stateHandle.get<Boolean>(PostsListFragment.FAVORITE)
            ?: throw IllegalStateException("Favorite arg not found in state handle")
    }

    @Provides
    fun getAllPostsUseCaseProvider(repository: PostRepository) = GetAllPosts(repository)

    @Provides
    fun deleteAllPostsUseCaseProvider(repository: PostRepository) = DeleteAllPosts(repository)

    @Provides
    fun syncPostsUseCaseProvider(repository: PostRepository) = SyncPosts(repository)

}