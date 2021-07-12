package com.duberton.adapter.output.mongo.config

import com.duberton.adapter.output.mongo.AlbumRepository
import com.duberton.adapter.output.mongo.UserRepository
import com.duberton.application.port.output.AlbumRepositoryPort
import com.duberton.application.port.output.UserRepositoryPort
import com.mongodb.MongoClient
import io.ktor.config.ApplicationConfig
import org.koin.dsl.module

fun mongoModule(applicationConfig: ApplicationConfig) = module {
    single { buildMongoClient(applicationConfig) }
    single<AlbumRepositoryPort> { AlbumRepository(get()) }
    single<UserRepositoryPort> { UserRepository(get()) }
}

fun buildMongoClient(applicationConfig: ApplicationConfig): MongoClient {
    val mongoHost = applicationConfig.property("ktor.mongo.host").getString()
    val mongoPort = applicationConfig.property("ktor.mongo.port").getString()
    return MongoClient(mongoHost, mongoPort.toInt())
}
