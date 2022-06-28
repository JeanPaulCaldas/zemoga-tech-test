package com.zemoga.core.data.author

import com.zemoga.core.domain.MockAuthor
import com.zemoga.core.domain.MockPost
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.coVerifyOrder
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
internal class AuthorRepositoryTest {
    @MockK
    lateinit var cacheSource: AuthorCacheSource

    @MockK
    lateinit var remoteSource: AuthorRemoteSource

    lateinit var repository: AuthorRepository

    @BeforeEach
    fun setUp() {
        repository = AuthorRepository(cacheSource, remoteSource)
    }

    @Test
    @DisplayName("Given a call should cache authors from remote")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun syncAuthors() = runTest {
        //arrange
        val authors = MockAuthor.mockList(7)
        coEvery { remoteSource.fetchAuthors() } returns authors
        coJustRun { cacheSource.persistAuthors(any()) }
        //act
        repository.syncAuthors()
        //assert
        coVerifyOrder {
            remoteSource.fetchAuthors()
            cacheSource.persistAuthors(authors)
        }
    }

    @Test
    @DisplayName("Given it's id should return a cached author")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAuthor() = runTest {
        //arrange
        val authorId = 3
        val author = MockAuthor(id = authorId).create()
        coEvery { cacheSource.getAuthor(any()) } returns author
        //act
        val result = repository.getAuthor(authorId)
        //assert
        coVerify(exactly = 1) { cacheSource.getAuthor(authorId) }
        assertEquals(author, result)
    }
}