package com.duberton.adapter.output.mongo.config

import com.duberton.adapter.output.mongo.AlbumRepository
import com.duberton.application.port.output.AlbumRepositoryPort
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import io.ktor.config.ApplicationConfig
import org.bson.Document
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun mongoModule(applicationConfig: ApplicationConfig) = module {
    single { buildMongoClient(applicationConfig) }
    single<AlbumRepositoryPort> { AlbumRepository(get(named("albumRepository"))) }
    single(named("albumRepository")) { buildAlbumCollection(get()) }
}

fun buildMongoClient(applicationConfig: ApplicationConfig): MongoClient {
    val mongoHost = applicationConfig.property("ktor.mongo.host").getString()
    val mongoPort = applicationConfig.property("ktor.mongo.port").getString()
    return MongoClient(mongoHost, mongoPort.toInt())
}

fun buildAlbumCollection(mongoClient: MongoClient): MongoCollection<Document> =
    mongoClient.getDatabase("bandcamper").getCollection("album")