package com.zemoga.posts.framework.retrofit.entities

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    @SerializedName("userId") val authorId: Int,
    val title: String,
    @SerializedName("body") val description: String
)
