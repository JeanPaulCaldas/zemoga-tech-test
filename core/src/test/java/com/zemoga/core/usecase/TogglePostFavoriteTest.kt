package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.MockPost
import com.zemoga.core.domain.Post
import io.mockk.*
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
internal class TogglePostFavoriteTest {

    @MockK
    lateinit var repository: PostRepository

    lateinit var post: Post

    private lateinit var useCase: TogglePostFavorite

    @BeforeEach
    fun setUp() {
        useCase = TogglePostFavorite(repository)
        post = MockPost().create()
    }

    @Test
    @DisplayName("Given succeed post toggle favorite should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        val updatedPost = post.copy(favorite = post.favorite.not())
        coJustRun { repository.toggleFavoritePostStatus(post) }
        coEvery { repository.getPost(any()) } returns updatedPost
        //act
        val result = useCase.invoke(post)
        //assert
        coVerifyOrder {
            repository.toggleFavoritePostStatus(post)
            repository.getPost(post.id)
        }
        assertNotEquals(result, Result.success(post))
        assertEquals(Result.success(updatedPost), result)
    }

    @Test
    @DisplayName("Given thrown exception while post toggle favorite should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coJustRun { repository.toggleFavoritePostStatus(post) }
        coEvery { repository.getPost(any()) } throws exception
        //act
        val result = useCase.invoke(post)
        //assert
        coVerifyOrder {
            repository.toggleFavoritePostStatus(post)
            repository.getPost(post.id)
        }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}