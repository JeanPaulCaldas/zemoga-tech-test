package com.zemoga.core.data.post

import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.domain.MockPost
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class PostRepositoryTest {
    @MockK
    lateinit var cacheSource: PostCacheSource

    @MockK
    lateinit var remoteSource: PostRemoteSource

    @MockK
    lateinit var authorRepository: AuthorRepository

    lateinit var repository: PostRepository

    @BeforeEach
    fun setUp() {
        repository = PostRepository(cacheSource, remoteSource, authorRepository)
    }

    @Nested
    @DisplayName("Getting posts cache/remote strategy")
    inner class GetPosts {
        @Test
        @DisplayName("Given empty cache should request remote source")
        @OptIn(ExperimentalCoroutinesApi::class)
        fun getPostsFromRemote() = runTest {
            //arrange
            val posts = MockPost.mockList(5)
            val postsFlow = flowOf(posts)
            coEvery { cacheSource.isEmpty() } returns true
            coEvery { remoteSource.fetchAllPosts() } returns posts
            coJustRun { cacheSource.persistPosts(any()) }
            coJustRun { authorRepository.syncAuthors() }
            coEvery { cacheSource.allPosts } returns postsFlow
            //act
            val result = repository.getPosts(false)
            //assert
            coVerifyOrder {
                cacheSource.isEmpty()
                remoteSource.fetchAllPosts()
                cacheSource.persistPosts(posts)
                authorRepository.syncAuthors()
                cacheSource.allPosts
            }
            coVerify(exactly = 0) { cacheSource.favoritePosts }
            assertEquals(postsFlow, result)
        }

        @Test
        @DisplayName("Given cached posts shouldn't request remote source")
        @OptIn(ExperimentalCoroutinesApi::class)
        fun getPostsCache() = runTest {
            //arrange
            val posts = MockPost.mockList(7)
            val postsFlow = flowOf(posts)
            coEvery { cacheSource.isEmpty() } returns false
            coEvery { cacheSource.allPosts } returns postsFlow
            //act
            val result = repository.getPosts(false)
            //assert
            coVerifyOrder {
                cacheSource.isEmpty()
                cacheSource.allPosts
            }
            coVerify(exactly = 0) {
                remoteSource.fetchAllPosts()
                cacheSource.persistPosts(posts)
                authorRepository.syncAuthors()
                cacheSource.favoritePosts
            }
            assertEquals(postsFlow, result)
        }
    }

    @Test
    @DisplayName("Given its id should return a post")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPost() = runTest {
        //arrange
        val postId = 3
        val post = MockPost(id = postId).create()
        coEvery { cacheSource.getPost(any()) } returns post
        //act
        val result = repository.getPost(postId)
        //assert
        coVerify(exactly = 1) { cacheSource.getPost(postId) }
        assertEquals(post, result)
    }

    @Test
    @DisplayName("Given its id should return all post comments")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPostComments() = runTest {
        //arrange
        val postId = 3
        val postComments = MockPost.mockPostComments()
        coEvery { remoteSource.fetchPostComments(postId) } returns postComments
        //act
        val result = repository.getPostComments(postId)
        //assert
        coVerify(exactly = 1) { remoteSource.fetchPostComments(postId) }
        assertEquals(postComments, result)
    }

    @Test
    @DisplayName("Given a call should request remote posts and authors")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun syncPosts() = runTest {
        //arrange
        val posts = MockPost.mockList(5)
        coEvery { remoteSource.fetchAllPosts() } returns posts
        coJustRun { cacheSource.persistPosts(any()) }
        coJustRun { authorRepository.syncAuthors() }
        //act
        repository.syncPosts()
        //assert
        coVerify(exactly = 1) {
            remoteSource.fetchAllPosts()
            cacheSource.persistPosts(posts)
            authorRepository.syncAuthors()
        }
    }

    @Test
    @DisplayName("Given a call should delete all cached posts")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteCachedPosts() = runTest {
        //arrange
        coJustRun { cacheSource.deleteAllPosts() }
        //act
        repository.deleteCachedPosts()
        //assert
        coVerify(exactly = 1) { cacheSource.deleteAllPosts() }
    }

    @Test
    @DisplayName("Given it's id should update a cached post")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun toggleFavPost() = runTest {
        //arrange
        val post = MockPost().create()
        val toggledPost = post.copy(favorite = post.favorite.not())
        coJustRun { cacheSource.updatePost(any()) }
        //act
        repository.toggleFavoritePostStatus(post)
        //assert
        coVerify(exactly = 1) { cacheSource.updatePost(toggledPost) }
    }

    @Test
    @DisplayName("Given it's id should delete a cached post")
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deletePost() = runTest {
        //arrange
        val post = MockPost().create()
        coJustRun { cacheSource.deletePost(any()) }
        //act
        repository.deletePost(post)
        //assert
        coVerify(exactly = 1) { cacheSource.deletePost(post) }
    }
}