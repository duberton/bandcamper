package com.duberton.adapter.output.redis

import com.duberton.adapter.output.redis.entity.UserCacheEntity
import com.duberton.adapter.output.redis.ext.toCacheEntity
import com.duberton.adapter.output.redis.ext.toDomain
import com.duberton.application.domain.User
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.concurrent.TimeUnit
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec

class UserCacheRepository(redissonClient: RedissonClient) : UserCacheRepositoryPort {

    private val map = redissonClient.getMap<String, UserCacheEntity>(
        "bandcamper",
        TypedJsonJacksonCodec(UserCacheEntity::class.java, UserCacheEntity::class.java, jacksonObjectMapper())
    )

    override fun save(user: User) {
        map[user.email] = user.toCacheEntity()
        map.expire(1L, TimeUnit.MINUTES)
    }

    override fun update(user: User) {
        val userEntity = user.toCacheEntity()
        map.putIfExists(userEntity.email, userEntity)
    }

    override fun findByEmail(email: String): User? {
        return map[email]?.toDomain()
    }
}