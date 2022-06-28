package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.MockPost
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GetAllPostsTest {
    @MockK
    lateinit var repository: PostRepository

    private lateinit var useCase: GetAllPosts

    @BeforeEach
    fun setUp() {
        useCase = GetAllPosts(repository)
    }

    @Test
    @DisplayName("Given succeed getting all posts should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        val favoriteFlag = false
        val posts = MockPost.mockList(5)
        val postsFlow = flowOf(posts)
        coEvery { repository.getPosts(any()) } returns postsFlow
        //act
        val result = useCase.invoke(favorites = favoriteFlag)
        //assert
        coVerify { repository.getPosts(favoriteFlag) }
        assertEquals(Result.success(postsFlow), result)
    }

    @Test
    @DisplayName("Given thrown exception while getting all posts should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val favoriteFlag = false
        val exception = Exception("mock exception")
        coEvery { repository.getPosts(any()) } throws exception
        //act
        val result = useCase.invoke(favoriteFlag)
        //assert
        coVerify { repository.getPosts(favoriteFlag) }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}