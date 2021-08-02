package com.duberton.application.port.output

import com.duberton.application.domain.User

interface UserRepositoryPort {

    fun save(user: User)

    fun update(user: User)

    fun findByEmail(email: String): User?
}
