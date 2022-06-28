package com.zemoga.core.usecase

import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.Author
import com.zemoga.core.domain.MockAuthor
import com.zemoga.core.domain.MockPost
import com.zemoga.core.domain.Post
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetPostDetailsTest {
    @MockK
    lateinit var postRepo: PostRepository

    @MockK
    lateinit var authorRepo: AuthorRepository

    private val postId: Int = 5

    private lateinit var post: Post

    lateinit var author: Author

    private lateinit var useCase: GetPostDetails

    @BeforeEach
    fun setUp() {
        useCase = GetPostDetails(postRepo, authorRepo)
        post = MockPost(id = postId).create()
        author = MockAuthor(post.authorId).create()
    }

    @Test
    @DisplayName("Given succeed post detail query should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        val resultPair = Pair(post, author)
        coEvery { postRepo.getPost(any()) } returns post
        coEvery { authorRepo.getAuthor(any()) } returns author
        //act
        val result = useCase.invoke(postId)
        //assert
        coVerifyOrder {
            postRepo.getPost(postId)
            authorRepo.getAuthor(post.authorId)
        }
        assertEquals(Result.success(resultPair), result)
    }

    @Test
    @DisplayName("Given thrown exception while post detail query should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coEvery { postRepo.getPost(any()) } throws exception
        //act
        val result = useCase.invoke(postId)
        //assert
        coVerify { postRepo.getPost(postId) }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}