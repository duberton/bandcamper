package com.duberton.adapter.input.api.v1.response

import com.duberton.adapter.input.api.jsonapi.response.BaseResponse

data class AlbumResponse(
    override val id: String?,
    val artist: String?,
    val title: String?,
    val url: String,
    val isReleased: Boolean?,
    val albumCoverUrl: String?,
    val releaseDate: String,
    val createdAt: String?,
    val updatedAt: String?
) : BaseResponse(id) {

    override fun type() = "Album"
}