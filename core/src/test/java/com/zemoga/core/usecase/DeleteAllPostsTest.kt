package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


@ExtendWith(MockKExtension::class)
internal class DeleteAllPostsTest {

    @MockK
    lateinit var repository: PostRepository

    private lateinit var useCase: DeleteAllPosts

    @BeforeEach
    fun setUp() {
        useCase = DeleteAllPosts(repository)
    }

    @Test
    @DisplayName("Given succeed all posts deletions should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        coJustRun { repository.deleteCachedPosts() }
        //act
        val result = useCase.invoke()
        //assert
        coVerify(exactly = 1) { repository.deleteCachedPosts() }
        assertEquals(Result.success(Unit), result)
    }

    @Test
    @DisplayName("Given thrown exception while all posts deletions should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coEvery { repository.deleteCachedPosts() } throws exception
        //act
        val result = useCase.invoke()
        //assert
        coVerify(exactly = 1) { repository.deleteCachedPosts() }
        assertEquals(Result.failure<Exception>(exception), result)
    }

}