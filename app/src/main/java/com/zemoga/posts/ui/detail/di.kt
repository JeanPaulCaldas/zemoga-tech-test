package com.zemoga.posts.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.usecase.GetAllPosts
import com.zemoga.core.usecase.GetPostDetails
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class DetailFragmentModule {

    @Provides
    @Named("postId")
    fun postIdProvider(stateHandle: SavedStateHandle): Int {
        return stateHandle.get<Int>(PostDetailFragment.POST_ID)
            ?: throw IllegalStateException("Post Id arg not found in state handle")

    }

    @Provides
    fun getPostDetailsUseCaseProvider(
        postRepository: PostRepository,
        authorRepository: AuthorRepository
    ) = GetPostDetails(postRepository, authorRepository)
}