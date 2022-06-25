package com.zemoga.posts.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeletePost
import com.zemoga.core.usecase.GetPostComments
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
    private val getPostComments: GetPostComments,
    private val deletePost: DeletePost,
    private val toggleFavorite: TogglePostFavorite
) : ViewModel() {

    private val _actionState = MutableLiveData<PostDetailActionState>()
    val actionState get() = _actionState

    private val _uiState = MutableStateFlow(PostDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDetails(postId).onFailure(::emitError)
                .onSuccess(::updateStatus)

            getPostComments(postId).onFailure(::emitError)
                .onSuccess(::updateStatus)
        }
    }

    fun setEvent(event: PostDetailEvent): Unit = when (event) {
        PostDetailEvent.DeletePost -> deleteAction()
        PostDetailEvent.ToggleFavorite -> toggleAction()
    }

    //region Private Methods
    private fun toggleAction() {
        viewModelScope.launch {
            toggleFavorite(_uiState.value.post!!).onFailure(::emitError)
                .onSuccess { post ->
                    _uiState.update { it.copy(post = post) }
                }
        }
    }

    private fun deleteAction() {
        viewModelScope.launch {
            deletePost(_uiState.value.post!!).onFailure(::emitError)
            _actionState.value = PostDetailActionState.NavigateOut
        }
    }

    private fun updateStatus(details: Pair<Post, Author>) {
        val (post, author) = details
        _uiState.update {
            it.copy(post = post, author = author)
        }
    }

    private fun updateStatus(comments: List<String>) {
        _uiState.update {
            it.copy(comments = comments)
        }
    }

    private fun emitError(throwable: Throwable) {
        _actionState.value = PostDetailActionState.Error(throwable)
    }
    //endregion

    //region Event Classes
    sealed class PostDetailActionState {
        object NavigateOut : PostDetailActionState()
        data class Error(val throwable: Throwable? = null) : PostDetailActionState()
    }

    data class PostDetailUiState(
        val comments: List<String> = listOf(),
        val post: Post? = null,
        val author: Author? = null
    ) {
        val isFavoritePost: Boolean get() = post?.favorite ?: false
    }

    sealed class PostDetailEvent {
        object ToggleFavorite : PostDetailEvent()
        object DeletePost : PostDetailEvent()
    }
    //endregion
}