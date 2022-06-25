package com.zemoga.core.usecase

import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostRepository

class GetPostDetails(
    private val postRepo: PostRepository,
    private val authorRepo: AuthorRepository
) {
    suspend operator fun invoke(postId: Int) = resultOf {
        val post = postRepo.getPost(postId)
        val author = authorRepo.getAuthor(post.authorId)
        Pair(post, author)
    }
}