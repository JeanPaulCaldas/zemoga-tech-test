package com.zemoga.posts.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.GetAllPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PostListViewModel @Inject constructor(
    @Named("favorite") private val favorite: Boolean,
    private val postsUseCase: GetAllPosts
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            postsUseCase(favorites = favorite).onEach { posts ->
                _uiState.update { it.copy(posts = posts) }
            }.collect()
        }
    }

    data class PostListUIState(
        val posts: List<Post> = listOf()
    )

}