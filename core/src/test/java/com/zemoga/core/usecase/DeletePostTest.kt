package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.Post
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DeletePostTest {
    @MockK
    lateinit var repository: PostRepository

    @MockK
    lateinit var post: Post

    private lateinit var useCase: DeletePost

    @BeforeEach
    fun setUp() {
        useCase = DeletePost(repository)
    }

    @Test
    @DisplayName("Given succeed posts deletion should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        coJustRun { repository.deletePost(any()) }
        //act
        val result = useCase.invoke(post)
        //assert
        coVerify(exactly = 1) { repository.deletePost(post) }
        assertEquals(Result.success(Unit), result)
    }

    @Test
    @DisplayName("Given thrown exception while all posts deletions should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coEvery { repository.deletePost(any()) } throws exception
        //act
        val result = useCase.invoke(post)
        //assert
        coVerify(exactly = 1) { repository.deletePost(post) }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}