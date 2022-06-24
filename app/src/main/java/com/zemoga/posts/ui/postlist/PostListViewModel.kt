package com.zemoga.posts.ui.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.GetPosts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    //private val favorites: Boolean,
    private val postsUseCase: GetPosts
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostListUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            postsUseCase.invoke(favorites = false)
                .onEach { posts ->
                    _uiState.update { it.copy(posts = posts) }
                }.collect()
        }
    }
}

data class PostListUIState(
    val posts: List<Post> = listOf()
)