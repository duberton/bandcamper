package com.duberton.adapter.output.redis

import com.duberton.adapter.output.redis.entity.UserCacheEntity
import com.duberton.adapter.output.redis.ext.toCacheEntity
import com.duberton.adapter.output.redis.ext.toDomain
import com.duberton.application.domain.User
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec
import java.util.concurrent.TimeUnit

const val TTL_SAVE = 60L

class UserCacheRepository(private val redissonClient: RedissonClient) : UserCacheRepositoryPort {

    private val codec = TypedJsonJacksonCodec(UserCacheEntity::class.java, jacksonObjectMapper())

    override fun save(user: User) {
        val bucket = redissonClient.getBucket<UserCacheEntity>(user.email, codec)
        bucket.set(user.toCacheEntity(), TTL_SAVE, TimeUnit.MINUTES)
    }

    override fun update(user: User) {
        val bucket = redissonClient.getBucket<UserCacheEntity>(user.email, codec)
        val userEntity = user.toCacheEntity()
        bucket.setAndKeepTTL(userEntity)
    }

    override fun findByEmail(email: String): User? {
        return redissonClient.getBucket<UserCacheEntity>(email, codec).get()?.toDomain()
    }
}
