package com.duberton.adapter.input.api.v1.ext

import com.duberton.adapter.input.api.jsonapi.response.ManyResourcesResponse
import com.duberton.adapter.input.api.jsonapi.response.SingleResourceResponse
import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.adapter.input.api.v1.request.UserInfoRequest
import com.duberton.adapter.input.api.v1.response.AlbumResponse
import com.duberton.application.domain.Album
import com.duberton.application.domain.User

fun AlbumRequest.toDomain() = Album(
    url = url
)

fun Album.toSingleResponse() = SingleResourceResponse(
    data = SingleResourceResponse.ResourceData(
        attributes = AlbumResponse(
            id = id,
            url = url,
            artist = artist,
            title = title,
            isReleased = isReleased,
            albumCoverUrl = albumCoverUrl,
            releaseDate = releaseDate.toString(),
            createdAt = createdAt.toString(),
            updatedAt = updatedAt?.toString()
        )
    )
)

fun List<Album>.toManyResponse(previous: String?, limit: Int) = ManyResourcesResponse(
    data = if (size > limit) dropLast(1).map { it.toResponse() } else map { it.toResponse() },
    cursors = ManyResourcesResponse.Cursors(
        previous = if (size > limit) firstOrNull()?.createdAt?.toString() else null,
        next = if (size > limit) dropLast(1).lastOrNull()?.createdAt?.toString() else null
    )
)

fun Album.toResponse() = ManyResourcesResponse.Data(
    attributes = AlbumResponse(
        id = id,
        url = url,
        artist = artist,
        title = title,
        isReleased = isReleased,
        albumCoverUrl = albumCoverUrl,
        releaseDate = releaseDate.toString(),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt?.toString()
    )
)

fun UserInfoRequest.toDomain() = User(
    googleId = id,
    fullName = name,
    pictureUrl = picture,
    country = locale,
    email = email
)