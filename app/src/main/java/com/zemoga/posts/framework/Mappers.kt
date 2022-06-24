package com.zemoga.posts.framework

import com.zemoga.core.domain.Post as DomainPost
import com.zemoga.posts.framework.database.Post as RoomPost
import com.zemoga.posts.framework.server.Post as ServerPost

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