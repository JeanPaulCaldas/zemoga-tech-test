package com.zemoga.posts.util

import com.zemoga.core.domain.Post

data class MockPost(val id: Int = 1, val favorite: Boolean = false) {

    fun create() = Post(
        id = id,
        authorId = 1,
        title = "Mock Post $id",
        description = "Description of a mocked post $id",
        favorite = favorite
    )

    companion object {
        fun mockPostComments(): List<String> {
            val comments = mutableListOf<String>()
            for (i in 1..5) {
                comments.add("Comment No $i")
            }
            return comments
        }

        fun mockList(postsNumber: Int): List<Post> {
            val posts = mutableListOf<Post>()
            for (i in 1..postsNumber) {
                posts.add(MockPost(id = i).create())
            }
            return posts
        }
    }
}