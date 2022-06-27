package com.zemoga.posts.ui.detail

import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.posts.ui.base.UiEffect
import com.zemoga.posts.ui.base.UiEvent
import com.zemoga.posts.ui.base.UiState

class PostDetailContract {

    sealed class Event : UiEvent {
        data class GetDetails(val postId: Int) : Event()
        object ToggleFavorite : Event()
        object DeletePost : Event()
    }

    data class State(
        val comments: List<String> = listOf(),
        val post: Post? = null,
        val author: Author? = null
    ) : UiState {
        val isFavoritePost: Boolean get() = post?.favorite ?: false
    }

    sealed class Effect : UiEffect {
        object NavigateOut : Effect()
        object ShowError : Effect()
    }
}