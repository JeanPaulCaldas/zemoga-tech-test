package com.zemoga.posts.ui.detail

import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeletePost
import com.zemoga.core.usecase.GetPostComments
import com.zemoga.core.usecase.GetPostDetails
import com.zemoga.core.usecase.TogglePostFavorite
import com.zemoga.posts.ui.base.BaseViewModel
import com.zemoga.posts.ui.detail.PostDetailContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getDetails: GetPostDetails,
    private val getPostComments: GetPostComments,
    private val deletePost: DeletePost,
    private val toggleFavorite: TogglePostFavorite
) : BaseViewModel<Event, State, Effect>() {

    //region MVI Contract
    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            Event.DeletePost -> delete()
            is Event.GetDetails -> getPostDetails(event.postId)
            Event.ToggleFavorite -> toggle()
        }
    }
    //endregion

    //region Private Methods
    private fun getPostDetails(postId: Int) {
        viewModelScope.launch {
            getDetails(postId).onFailure { emitError() }
                .onSuccess(::updateStatus)

            getPostComments(postId).onFailure { emitError() }
                .onSuccess(::updateStatus)
        }
    }

    private fun toggle() {
        viewModelScope.launch {
            currentState.post?.let { post ->
                toggleFavorite(post).onFailure { emitError() }
                    .onSuccess {
                        setState { copy(post = it) }
                    }
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            currentState.post?.let {
                deletePost(it).onFailure { emitError() }
                setEffect { Effect.NavigateOut }
            }
        }
    }

    private fun updateStatus(details: Pair<Post, Author>) {
        val (post, author) = details
        setState { copy(post = post, author = author) }
    }

    private fun updateStatus(comments: List<String>) {
        setState { copy(comments = comments) }
    }

    private fun emitError() {
        setEffect { Effect.ShowError }
    }
    //endregion
}