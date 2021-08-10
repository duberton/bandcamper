package com.duberton.adapter.output.mongo

import com.duberton.adapter.output.mongo.ext.toAlbumDomain
import com.duberton.adapter.output.mongo.ext.toDocument
import com.duberton.application.domain.Album
import com.duberton.application.port.output.AlbumRepositoryPort
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import org.bson.Document

class AlbumRepository(private val mongoCollection: MongoCollection<Document>) : AlbumRepositoryPort {


    override fun save(album: Album) {
        mongoCollection.insertOne(album.toDocument())
    }

    override fun findByEmail(email: String): List<Album> {
        return mongoCollection.find(eq("email", email)).map { it.toAlbumDomain() }.toList()
    }

    override fun findByReleaseDate(releaseDate: String): List<Album> {
        return mongoCollection.find(eq("releaseDate", releaseDate)).map { it.toAlbumDomain() }.toList()
    }
}