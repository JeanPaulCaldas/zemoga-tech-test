package com.zemoga.posts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeleteAllPosts
import com.zemoga.core.usecase.GetAllPosts
import com.zemoga.core.usecase.SyncPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PostListViewModel @Inject constructor(
    @Named("favorite") private val favorite: Boolean,
    private val getAllUseCase: GetAllPosts,
    private val deleteAllUseCase: DeleteAllPosts,
    private val syncPosts: SyncPosts
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllUseCase(favorites = favorite).onEach { posts ->
                _uiState.update { it.copy(posts = posts) }
            }.collect()
        }
    }

    fun setEvent(event: PostListEvent): Unit = when (event) {
        PostListEvent.DeleteAll -> deleteAction()
        PostListEvent.Refresh -> refreshAction()
    }

    private fun refreshAction() {
        viewModelScope.launch { syncPosts() }
    }

    private fun deleteAction() {
        viewModelScope.launch { deleteAllUseCase() }
    }

    sealed class PostListEvent {
        object DeleteAll : PostListEvent()
        object Refresh : PostListEvent()
    }

    data class PostListUIState(
        val posts: List<Post> = listOf()
    )

}