package com.duberton.adapter.output.redis.entity

open class UserCacheEntity(
    val googleId: String,
    val fullName: String,
    val pictureUrl: String,
    val country: String,
    val email: String
)
