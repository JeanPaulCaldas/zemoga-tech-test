package com.zemoga.posts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeleteAllPosts
import com.zemoga.core.usecase.GetAllPosts
import com.zemoga.core.usecase.SyncPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PostListViewModel @Inject constructor(
    @Named("favorite") private val favorite: Boolean,
    private val getAllPosts: GetAllPosts,
    private val deleteAllPosts: DeleteAllPosts,
    private val syncPosts: SyncPosts
) : ViewModel() {

    private val _uiState = MutableStateFlow<PostListUIState>(PostListUIState.Posts())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllPosts(favorites = favorite)
                .onSuccess { it.onEach(::updateState).collect() }
                .onFailure(::emitError)
        }
    }

    fun setEvent(event: PostListEvent): Unit = when (event) {
        PostListEvent.DeleteAll -> deleteAction()
        PostListEvent.Refresh -> refreshAction()
    }

    //region Private Methods
    private fun updateState(posts: List<Post>) {
        _uiState.value = PostListUIState.Posts(posts)
    }

    private fun emitError(throwable: Throwable) {
        _uiState.value = PostListUIState.Error(throwable)
    }

    private fun refreshAction() {
        viewModelScope.launch {
            syncPosts().onFailure(::emitError)
        }
    }

    private fun deleteAction() {
        viewModelScope.launch {
            deleteAllPosts().onFailure(::emitError)
        }
    }
    //endregion

    //region Event Classes
    sealed class PostListEvent {
        object DeleteAll : PostListEvent()
        object Refresh : PostListEvent()
    }

    sealed class PostListUIState {
        data class Posts(val posts: List<Post> = listOf()) : PostListUIState()
        data class Error(val throwable: Throwable? = null) : PostListUIState()
    }
    //endregion
}