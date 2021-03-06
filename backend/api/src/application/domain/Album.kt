package com.duberton.application.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Album(
    val id: String? = null,
    val url: String = "",
    var artist: String? = null,
    var title: String? = null,
    var releaseDate: LocalDate? = null,
    var isReleased: Boolean? = null,
    var albumCoverUrl: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    var email: String? = null
)