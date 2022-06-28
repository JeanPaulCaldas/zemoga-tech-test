package com.zemoga.posts.ui.detail

import com.zemoga.core.domain.Author
import com.zemoga.core.domain.Post
import com.zemoga.core.usecase.DeletePost
import com.zemoga.core.usecase.GetPostComments
import com.zemoga.core.usecase.GetPostDetails
import com.zemoga.core.usecase.TogglePostFavorite
import com.zemoga.posts.util.MockAuthor
import com.zemoga.posts.util.MockPost
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class PostDetailViewModelTest {

    @MockK
    lateinit var getDetails: GetPostDetails

    @MockK
    lateinit var getPostComments: GetPostComments

    @MockK
    lateinit var deletePost: DeletePost

    @MockK
    lateinit var toggleFavorite: TogglePostFavorite

    lateinit var viewModel: PostDetailViewModel
    private val postId = 3
    private lateinit var post: Post
    private lateinit var author: Author
    lateinit var comments: List<String>

    @BeforeEach
    fun setUp() {
        viewModel = PostDetailViewModel(getDetails, getPostComments, deletePost, toggleFavorite)

        val post = MockPost(id = postId).create()
        val author = MockAuthor(post.authorId).create()
        val comments = MockPost.mockPostComments()
    }

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getDetails() = runTest() {
        //arrange
        val pair = Pair(post, author)
        coEvery { getDetails.invoke(any()) } returns Result.success(pair)
        coEvery { getPostComments.invoke(any()) } returns Result.success(listOf())
        //act
        viewModel.setEvent(PostDetailContract.Event.GetDetails(postId))
        //assert
        val state = PostDetailContract.State(comments, post, author)
        assertEquals(state, viewModel.currentState)
    }
}