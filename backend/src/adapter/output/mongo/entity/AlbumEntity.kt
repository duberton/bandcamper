package com.duberton.adapter.output.mongo.entity

import java.time.LocalDate
import java.time.LocalDateTime
import org.bson.codecs.pojo.annotations.BsonId

data class AlbumEntity(
    @BsonId
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