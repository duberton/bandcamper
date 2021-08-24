package com.duberton.adapter.output.mongo

import com.duberton.adapter.output.mongo.ext.toAlbumDomain
import com.duberton.adapter.output.mongo.ext.toDocument
import com.duberton.application.domain.Album
import com.duberton.application.port.output.AlbumRepositoryPort
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import java.time.LocalDateTime
import org.bson.Document
import org.litote.kmongo.aggregate
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import org.litote.kmongo.gt
import org.litote.kmongo.limit
import org.litote.kmongo.lt
import org.litote.kmongo.match
import org.litote.kmongo.sort

class AlbumRepository(private val mongoCollection: MongoCollection<Document>) : AlbumRepositoryPort {

    override fun save(album: Album) {
        mongoCollection.insertOne(album.toDocument())
    }

    override fun findByEmail(email: String): List<Album> {
        return mongoCollection.find(eq(Album::email eq email)).map { it.toAlbumDomain() }.toList()
    }

    override fun findByEmailWithCursor(email: String, previous: String?, next: String?, limit: Int): List<Album> {
        return mongoCollection.aggregate<Album>(
            *listOfNotNull(
                previous?.let { match(Album::createdAt lt LocalDateTime.parse(previous)) },
                next?.let { match(Album::createdAt gt LocalDateTime.parse(next)) }
            ).toTypedArray(),
            match(Album::email eq email),
            limit(limit + 1),
            sort(ascending(Album::createdAt))
        ).toList()
    }

    override fun findByReleaseDate(releaseDate: String): List<Album> {
        return mongoCollection.find(eq("releaseDate", releaseDate)).map { it.toAlbumDomain() }.toList()
    }
}