package com.zemoga.posts.ui.postlist

import androidx.lifecycle.SavedStateHandle
import com.zemoga.core.data.PostRepository
import com.zemoga.core.usecase.GetPosts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class ListFragmentModule {

    /*@Provides
    @Named(FAVORITES)
    fun favoritesProvider(stateHandle: SavedStateHandle):Boolean{
        return stateHandle.get<Boolean>("") ?: throw IllegalStateException("Favorite arg not found in state handle")

    }*/

    @Provides
    fun postListViewModelProvider(
        //@Named(FAVORITES) favorites:Boolean,
        postRepository: PostRepository
    ) = GetPosts(postRepository)


    companion object {
        const val FAVORITES = "favorites"
    }
}