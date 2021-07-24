package com.duberton.adapter.output.redis.config

import com.duberton.adapter.output.redis.UserCacheRepository
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.koin.dsl.module
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.JsonJacksonCodec
import org.redisson.config.Config

val redisModule = module {
    single { buildRedissonClient() }
    single<UserCacheRepositoryPort> { UserCacheRepository(get()) }
}

fun buildRedissonClient(): RedissonClient {
    val config = Config()
    val jsonJacksonCodec = JsonJacksonCodec(jacksonObjectMapper())
    config.codec = jsonJacksonCodec
    config.useSingleServer().address = "redis://localhost:6379"
    return Redisson.create(config)
}
