package com.duberton.adapter.output.mongo.ext

import com.duberton.application.domain.Album
import org.bson.Document

fun Album.toDocument() = Document().apply {
    append("url", url)
    append("artist", artist)
    append("title", title)
    append("releaseDate", releaseDate)
    append("isReleased", isReleased)
}

fun Document.toDomain() = Album(
    id = getObjectId("_id").toString(),
    artist = getString("artist"),
    title = getString("title"),
    url = getString("url"),
    releaseDate = getString("releaseDate"),
    isReleased = getBoolean("isReleased")
)