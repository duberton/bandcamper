package com.duberton.adapter.output.mongo

import com.duberton.adapter.output.mongo.ext.toAlbumDomain
import com.duberton.adapter.output.mongo.ext.toDocument
import com.duberton.application.domain.Album
import com.duberton.application.port.output.AlbumRepositoryPort
import com.mongodb.MongoClient
import com.mongodb.client.model.Filters.eq

class AlbumRepository(mongoClient: MongoClient) : AlbumRepositoryPort {

    private val collection = mongoClient.getDatabase("bandcamper").getCollection("album")

    override fun save(album: Album) {
        collection.insertOne(album.toDocument())
    }

    override fun findByEmail(email: String): List<Album> {
        return collection.find(eq("email", email)).map { it.toAlbumDomain() }.toList()
    }
}