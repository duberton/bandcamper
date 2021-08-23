package com.duberton.adapter.input.api.v1.ext

import com.duberton.adapter.input.api.jsonapi.response.SingleResourceResponse
import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.adapter.input.api.v1.request.UserInfoRequest
import com.duberton.adapter.input.api.v1.response.AlbumResponse
import com.duberton.application.domain.Album
import com.duberton.application.domain.User

fun AlbumRequest.toDomain() = Album(
    url = url
)

fun Album.toResponse() = SingleResourceResponse(
    entity = SingleResourceResponse.ResponseData(
        data = SingleResourceResponse.ResponseData.Data(
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
)


fun UserInfoRequest.toDomain() = User(
    googleId = id,
    fullName = name,
    pictureUrl = picture,
    country = locale,
    email = email
)