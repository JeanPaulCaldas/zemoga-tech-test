package com.zemoga.core.domain

data class Post(
    val id: Int,
    val authorId: Int,
    val title: String,
    val description: String,
    val favorite: Boolean
)
