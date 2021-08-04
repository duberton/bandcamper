package com.duberton.adapter.output.redis.ext

import com.duberton.adapter.output.redis.entity.UserCacheEntity
import com.duberton.application.domain.User

fun User.toCacheEntity() = UserCacheEntity(
    googleId,
    fullName,
    pictureUrl,
    country,
    email
)

fun UserCacheEntity.toDomain() = User(
    googleId = googleId,
    fullName = fullName,
    pictureUrl = pictureUrl,
    country = country,
    email = email
)