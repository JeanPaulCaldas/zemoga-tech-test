package com.zemoga.core.usecase

import com.zemoga.core.data.post.PostRepository
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
internal class SyncPostsTest {
    @MockK
    lateinit var repository: PostRepository

    private lateinit var useCase: SyncPosts

    @BeforeEach
    fun setUp() {
        useCase = SyncPosts(repository)
    }

    @Test
    @DisplayName("Given succeed posts sync should return result success")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun test() = runTest {
        //arrange
        coJustRun { repository.syncPosts() }
        //act
        val result = useCase.invoke()
        //assert
        coVerify(exactly = 1) { repository.syncPosts() }
        assertEquals(Result.success(Unit), result)
    }

    @Test
    @DisplayName("Given thrown exception while posts sync should return result failure")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testException() = runTest {
        //arrange
        val exception = Exception("mock exception")
        coEvery { repository.syncPosts() } throws exception
        //act
        val result = useCase.invoke()
        //assert
        coVerify(exactly = 1) { repository.syncPosts() }
        assertEquals(Result.failure<Exception>(exception), result)
    }
}