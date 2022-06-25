package com.zemoga.posts.ui.list

import androidx.lifecycle.SavedStateHandle
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.usecase.GetAllPosts
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
    fun getAllPostsUseCaseProvider(postRepository: PostRepository) = GetAllPosts(postRepository)

}