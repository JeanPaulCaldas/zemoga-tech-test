package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.MockPost
import io.mockk.coEvery
import io.mockk.coVerify
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
internal class GetPostCommentsTest {
    @MockK
    lateinit var repository: PostRepository

    private val postId: Int = 5

    private lateinit var useCase: GetPostComments

    @BeforeEach
    fun setUp() {
        useCase = GetPostComments(repository)
    }

    @Test
    @DisplayName("Given succeed post comments query should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        val postComments = MockPost.mockPostComments()
        coEvery { repository.getPostComments(any()) } returns postComments
        //act
        val result = useCase.invoke(postId)
        //assert
        coVerify { repository.getPostComments(postId) }
        assertEquals(Result.success(postComments), result)
    }

    @Test
    @DisplayName("Given thrown exception while post comments query should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coEvery { repository.getPostComments(any()) } throws exception
        //act
        val result = useCase.invoke(postId)
        //assert
        coVerify { repository.getPostComments(postId) }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}