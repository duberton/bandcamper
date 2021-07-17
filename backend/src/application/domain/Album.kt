package com.duberton.application.domain

data class Album(
    val id: String? = null,
    val url: String,
    var artist: String? = null,
    var title: String? = null,
    var releaseDate: String? = null,
    var isReleased: Boolean? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    var email: String? = null
)