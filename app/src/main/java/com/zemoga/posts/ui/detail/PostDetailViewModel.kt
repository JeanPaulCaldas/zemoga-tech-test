package com.zemoga.posts.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.GetPostDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    @Named("postId") private val postId: Int,
    private val getPostDetailsUseCase: GetPostDetails
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val (post, author, comments) = getPostDetailsUseCase(postId)
            _uiState.update {
                it.copy(
                    post = post,
                    author = author,
                    comments = comments
                )
            }
        }
    }

    data class PostDetailUiState(
        val comments: List<String> = listOf(),
        val post: Post? = null,
        val author: Author? = null
    )
}