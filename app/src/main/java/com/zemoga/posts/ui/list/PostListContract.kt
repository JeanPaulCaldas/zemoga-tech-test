package com.zemoga.posts.ui.list

import com.zemoga.core.domain.Post
import com.zemoga.posts.ui.base.UiEffect
import com.zemoga.posts.ui.base.UiEvent
import com.zemoga.posts.ui.base.UiState

class PostListContract {

    sealed class Event : UiEvent {
        object GetAll : Event()
        object DeleteAll : Event()
        object Refresh : Event()
    }

    data class State(val state: PostListState) : UiState

    sealed class PostListState {
        object Idle : PostListState()
        object Loading : PostListState()
        data class Success(val posts: List<Post> = listOf()) : PostListState()
    }

    sealed class Effect : UiEffect {
        object ShowError : Effect()
    }
}