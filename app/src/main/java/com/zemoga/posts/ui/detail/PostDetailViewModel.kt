package com.zemoga.posts.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeletePost
import com.zemoga.core.usecase.GetPostDetails
import com.zemoga.core.usecase.TogglePostFavorite
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
    private val getDetails: GetPostDetails,
    private val deletePost: DeletePost,
    private val toggleFavorite: TogglePostFavorite
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val (post, author, comments) = getDetails(postId)
            _uiState.update {
                it.copy(
                    post = post,
                    author = author,
                    comments = comments
                )
            }
        }
    }

    fun setEvent(event: PostDetailEvent): Unit = when (event) {
        PostDetailEvent.DeletePost -> deleteAction()
        PostDetailEvent.ToggleFavorite -> toggleAction()
    }

    private fun toggleAction() {
        viewModelScope.launch {
            val post = toggleFavorite(_uiState.value.post!!)
            _uiState.update { it.copy(post = post) }
        }
    }

    private fun deleteAction() {
        viewModelScope.launch {
            deletePost(_uiState.value.post!!)
            _uiState.update { it.copy(navigateOut = true) }
        }
    }


    data class PostDetailUiState(
        val comments: List<String> = listOf(),
        val post: Post? = null,
        val author: Author? = null,
        val navigateOut: Boolean = false
    ) {
        val isFavoritePost: Boolean get() = post?.favorite ?: false
    }

    sealed class PostDetailEvent {
        object ToggleFavorite : PostDetailEvent()
        object DeletePost : PostDetailEvent()
    }
}