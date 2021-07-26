package com.duberton.adapter.output.mongo.ext

import com.duberton.application.domain.Album
import com.duberton.application.domain.User
import org.bson.Document
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Album.toDocument() = Document().apply {
    append("url", url)
    append("artist", artist)
    append("title", title)
    append("releaseDate", releaseDate)
    append("isReleased", isReleased)
    append("albumCoverUrl", albumCoverUrl)
    append("createdAt", createdAt)
    append("email", email)
}

fun Document.toAlbumDomain() = Album(
    id = getObjectId("_id").toString(),
    artist = getString("artist"),
    title = getString("title"),
    url = getString("url"),
    releaseDate = getString("releaseDate"),
    isReleased = getBoolean("isReleased"),
    albumCoverUrl = getString("albumCoverUrl"),
    createdAt = getDate("createdAt").toLocalDateTime(),
    updatedAt = getDate("updatedAt")?.toLocalDateTime()
)

fun User.toDocument() = Document().apply {
    append("googleId", googleId)
    append("fullName", fullName)
    append("pictureUrl", pictureUrl)
    append("country", country)
    append("email", email)
    append("createdAt", LocalDateTime.now())
}

fun User.toUpdateDocument() = Document("\$set", Document().apply {
    append("googleId", googleId)
    append("fullName", fullName)
    append("pictureUrl", pictureUrl)
    append("country", country)
    append("email", email)
    append("updatedAt", LocalDateTime.now())
})

fun Document.toUserDomain() = User(
    id = getObjectId("_id").toString(),
    googleId = getString("googleId"),
    fullName = getString("fullName"),
    pictureUrl = getString("pictureUrl"),
    country = getString("country"),
    email = getString("email"),
    createdAt = getDate("createdAt").toString(),
    updatedAt = getDate("updatedAt")?.toString()
)

fun Date.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault())