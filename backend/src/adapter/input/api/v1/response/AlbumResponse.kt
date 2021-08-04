package com.duberton.adapter.input.api.v1.response

data class AlbumResponse(
    val id: String?,
    val artist: String?,
    val title: String?,
    val url: String,
    val isReleased: Boolean?,
    val albumCoverUrl: String?,
    val releaseDate: String,
    val createdAt: String?,
    val updatedAt: String?
)