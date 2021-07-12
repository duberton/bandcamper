package com.duberton.application.domain

import com.duberton.application.port.output.UserRepositoryPort

data class User(
    val id: String? = null,
    val googleId: String,
    val fullName: String,
    val pictureUrl: String,
    val country: String,
    val email: String
) {

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