package com.duberton.adapter.input.api.v1.ext

import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.adapter.input.api.v1.response.AlbumResponse
import com.duberton.application.domain.Album

fun AlbumRequest.toDomain() = Album(
    url = url
)

fun Album.toResponse() = AlbumResponse(
    id = id,
    url = url,
    artist = artist,
    title = title,
    isReleased = isReleased,
    releaseDate = releaseDate.toString()
)