package com.zemoga.posts.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.usecase.DeletePost
import com.zemoga.core.usecase.GetPostComments
import com.zemoga.core.usecase.GetPostDetails
import com.zemoga.core.usecase.TogglePostFavorite
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
        val args = PostDetailFragmentArgs.fromSavedStateHandle(stateHandle)
        return args.postId
    }

    @Provides
    fun getPostDetailsUseCaseProvider(
        postRepository: PostRepository,
        authorRepository: AuthorRepository
    ) = GetPostDetails(postRepository, authorRepository)

    @Provides
    fun getPostCommentsUseCaseProvider(repository: PostRepository) =
        GetPostComments(repository)

    @Provides
    fun deletePostUseCaseProvider(repository: PostRepository) = DeletePost(repository)

    @Provides
    fun togglePostFavoriteUseCaseProvider(repository: PostRepository) =
        TogglePostFavorite(repository)
}