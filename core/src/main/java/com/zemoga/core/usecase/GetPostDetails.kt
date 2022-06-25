package com.zemoga.core.usecase

import com.zemoga.core.data.author.AuthorRepository
import com.zemoga.core.data.post.PostRepository
import com.zemoga.core.domain.PostDetails

class GetPostDetails(
    private val postRepo: PostRepository,
    private val authorRepo: AuthorRepository
) {
    suspend operator fun invoke(postId: Int): PostDetails {
        val post = postRepo.getPost(postId)
        val author = authorRepo.getAuthor(post.authorId)
        val comments = postRepo.getPostComments(postId)
        return PostDetails(post, author, comments)
    }
}