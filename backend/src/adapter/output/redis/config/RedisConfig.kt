package com.duberton.adapter.output.redis.config

import com.duberton.adapter.output.redis.UserCacheRepository
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.config.*
import org.koin.dsl.module
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config

fun redisModule(applicationConfig: ApplicationConfig) = module {
    single { buildRedissonClient(applicationConfig) }
    single<UserCacheRepositoryPort> { UserCacheRepository(get()) }
}

fun buildRedissonClient(applicationConfig: ApplicationConfig): RedissonClient {
    val redisHost = applicationConfig.property("ktor.redis.host").getString()
    val redisPort = applicationConfig.property("ktor.redis.port").getString()
    val config = Config()
    val jsonJacksonCodec = JsonJacksonCodec(jacksonObjectMapper())
    config.codec = jsonJacksonCodec
    config.useSingleServer().address = "redis://$redisHost:$redisPort"
    return Redisson.create(config)
}
