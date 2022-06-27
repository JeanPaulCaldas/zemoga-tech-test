package com.zemoga.posts.ui.list

import androidx.lifecycle.viewModelScope
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeleteAllPosts
import com.zemoga.core.usecase.GetAllPosts
import com.zemoga.core.usecase.SyncPosts
import com.zemoga.posts.ui.base.BaseViewModel
import com.zemoga.posts.ui.list.PostListContract.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : BaseViewModel<Event, State, Effect>() {

    //region MVI Contract
    override fun createInitialState(): State {
        return State(PostListState.Idle)
    }

    override fun handleEvent(event: Event) {
        setState { copy(state = PostListState.Loading) }
        when (event) {
            Event.GetAll -> getAll()
            Event.DeleteAll -> deleteAll()
            Event.Refresh -> refresh()
        }
    }
    //endregion

    //region Private Methods
    private fun updateState(posts: List<Post>) {
        setState { copy(state = PostListState.Success(posts)) }
    }

    private fun emitError() {
        setState { copy(state = PostListState.Idle) }
        setEffect { Effect.ShowError }
    }

    private fun getAll(){
        viewModelScope.launch {
            getAllPosts(favorites = favorite)
                .onSuccess { it.onEach(::updateState).collect() }
                .onFailure { emitError() }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            syncPosts().onFailure { emitError() }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch {
            deleteAllPosts().onFailure { emitError() }
        }
    }
    //endregion
}