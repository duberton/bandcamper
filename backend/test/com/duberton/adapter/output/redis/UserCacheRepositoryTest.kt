package com.duberton.adapter.output.redis

import com.duberton.adapter.output.redis.entity.UserCacheEntity
import com.duberton.common.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.redisson.RedissonBucket
import org.redisson.api.RedissonClient

class UserCacheRepositoryTest {

    private val redissonClient = mockk<RedissonClient>()

    private val userCacheRepository = UserCacheRepository(redissonClient)

    @Test
    fun `given a user that needs to be saved, when i do it, then it should save successfully`() {
        val redissonBucket = mockk<RedissonBucket<UserCacheEntity>>(relaxed = true)

        every { redissonClient.getBucket<UserCacheEntity>(any(), any()) } returns redissonBucket

        userCacheRepository.save(dummyObject())

        verify { redissonClient.getBucket<UserCacheEntity>(any(), any()) }
    }

    @Test
    fun `given an user that needs to be updated, when i do it, then it should save successfully`() {
        val redissonBucket = mockk<RedissonBucket<UserCacheEntity>>(relaxed = true)

        every { redissonClient.getBucket<UserCacheEntity>(any(), any()) } returns redissonBucket

        userCacheRepository.update(dummyObject())

        verify { redissonClient.getBucket<UserCacheEntity>(any(), any()) }
    }

    @Test
    fun `given an user that needs to be found, when i do it, then it should pass`() {
        every { redissonClient.getBucket<UserCacheEntity>(any(), any()).get() } returns dummyObject()

        userCacheRepository.findByEmail("email")

        verify { redissonClient.getBucket<UserCacheEntity>(any(), any()) }
    }
}