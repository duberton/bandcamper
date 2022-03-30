package com.duberton.adapter.output.mongo.config

import com.duberton.adapter.output.mongo.AlbumRepository
import com.duberton.application.port.output.AlbumRepositoryPort
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import io.ktor.config.ApplicationConfig
import org.bson.Document
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.KMongo

fun mongoModule(applicationConfig: ApplicationConfig) = module {
    single { buildMongoClient(applicationConfig) }
    single<AlbumRepositoryPort> { AlbumRepository(get(named(MONGO_ALBUM_REPO))) }
    single(named(MONGO_ALBUM_REPO)) { buildAlbumCollection(get()) }
}

fun buildMongoClient(applicationConfig: ApplicationConfig): MongoClient {
    val mongoHost = applicationConfig.property("ktor.mongo.host").getString()
    val mongoPort = applicationConfig.property("ktor.mongo.port").getString()
    return KMongo.createClient(
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://$mongoHost:$mongoPort"))
            .build()
    )
}

fun buildAlbumCollection(mongoClient: MongoClient): MongoCollection<Document> =
    mongoClient.getDatabase("bandcamper").getCollection("album")

const val MONGO_ALBUM_REPO = "mongoAlbumRepository"