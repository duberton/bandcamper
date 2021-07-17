package com.duberton.adapter.output.mongo.ext

import com.duberton.application.domain.Album
import com.duberton.application.domain.User
import org.bson.Document

fun Album.toDocument() = Document().apply {
    append("url", url)
    append("artist", artist)
    append("title", title)
    append("releaseDate", releaseDate)
    append("isReleased", isReleased)
}

fun Document.toAlbumDomain() = Album(
    id = getObjectId("_id").toString(),
    artist = getString("artist"),
    title = getString("title"),
    url = getString("url"),
    releaseDate = getString("releaseDate"),
    isReleased = getBoolean("isReleased")
)

fun User.toDocument() = Document().apply {
    append("googleId", googleId)
    append("fullName", fullName)
    append("pictureUrl", pictureUrl)
    append("country", country)
    append("email", email)
}

fun User.toUpdateDocument() = Document("\$set", Document().apply {
    append("googleId", googleId)
    append("fullName", fullName)
    append("pictureUrl", pictureUrl)
    append("country", country)
    append("email", email)
})

fun Document.toUserDomain() = User(
    id = getObjectId("_id").toString(),
    googleId = getString("googleId"),
    fullName = getString("fullName"),
    pictureUrl = getString("pictureUrl"),
    country = getString("country"),
    email = getString("email")
)