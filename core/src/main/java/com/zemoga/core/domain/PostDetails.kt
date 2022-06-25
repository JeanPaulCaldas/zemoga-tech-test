package com.zemoga.core.domain

data class PostDetails(val post: Post, val author: Author, val comments: List<String>)