package com.zemoga.posts.framework.util

import com.zemoga.core.domain.Author as DomainAuthor
import com.zemoga.core.domain.Post as DomainPost
import com.zemoga.posts.framework.room.entities.Author as RoomAuthor
import com.zemoga.posts.framework.room.entities.Post as RoomPost
import com.zemoga.posts.framework.retrofit.entities.Author as ServerAuthor
import com.zemoga.posts.framework.retrofit.entities.Post as ServerPost

internal fun RoomPost.toDomain() = DomainPost(
    id = id,
    authorId = authorId,
    title = title,
    description = description,
    favorite = favorite
)

internal fun ServerPost.toDomain() = DomainPost(
    id = id,
    authorId = authorId,
    title = title,
    description = description,
    favorite = false
)

internal fun DomainPost.toRoom() = RoomPost(
    id = id,
    authorId = authorId,
    title = title,
    description = description,
    favorite = favorite
)

internal fun RoomAuthor.toDomain() = DomainAuthor(
    id = id,
    name = name,
    email = email,
    phone = phone,
    website = website
)

internal fun DomainAuthor.toRoom() = RoomAuthor(
    id = id,
    name = name,
    email = email,
    phone = phone,
    website = website
)

internal fun ServerAuthor.toDomain() = DomainAuthor(
    id = id,
    name = name,
    email = email,
    phone = phone,
    website = website
)