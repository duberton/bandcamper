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

fun List<Album>.toManyResponse(previous: String?, next: String?, limit: Int) = ManyResourcesResponse(
    data = when {
        size > limit && previous == null -> dropLast(1).map { it.toResponse() }
        size > limit && previous != null -> dropLast(1).reversed().map { it.toResponse() }
        size == limit && previous == null -> map { it.toResponse() }
        size == limit && previous != null -> reversed().map { it.toResponse() }
        size < limit && previous == null -> map { it.toResponse() }
        size < limit && previous != null -> reversed().map { it.toResponse() }
        else -> map { it.toResponse() }
    },
    cursors = ManyResourcesResponse.Cursors(
        previous = when {
            size > limit && previous != null -> dropLast(1).reversed().firstOrNull()?.createdAt?.toString()
            size > limit && next != null -> dropLast(1).firstOrNull()?.createdAt?.toString()
            previous == null && next == null -> null
            size == limit && previous != null -> null
            else -> firstOrNull()?.createdAt?.toString()
        },
        next = when {
            size > limit -> dropLast(1).lastOrNull()?.createdAt?.toString()
            size == limit && previous != null -> reversed().lastOrNull()?.createdAt?.toString()
            size == limit && next != null -> null
            size < limit && next != null -> null
            size == limit && next == null -> null
            else -> lastOrNull()?.createdAt?.toString()
        }
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