package com.duberton.application.domain

import com.duberton.application.port.output.UserCacheRepositoryPort
import com.duberton.application.port.output.UserRepositoryPort

data class User(
    val id: String? = null,
    val googleId: String,
    val fullName: String,
    val pictureUrl: String,
    val country: String,
    val email: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
) {

    fun findUserByEmailCache(userCacheRepositoryPort: UserCacheRepositoryPort): User? {
        return userCacheRepositoryPort.findByEmail(email)
    }

    fun updateCache(userCacheRepositoryPort: UserCacheRepositoryPort) {
        return userCacheRepositoryPort.update(this)
    }

    fun saveCache(userCacheRepositoryPort: UserCacheRepositoryPort) {
        return userCacheRepositoryPort.save(this)
    }

    fun findUserByEmail(userRepositoryPort: UserRepositoryPort): User? {
        return userRepositoryPort.findByEmail(email)
    }

    fun save(userRepositoryPort: UserRepositoryPort) {
        userRepositoryPort.save(this)
    }

    fun update(userRepositoryPort: UserRepositoryPort) {
        userRepositoryPort.update(this)
    }
}
