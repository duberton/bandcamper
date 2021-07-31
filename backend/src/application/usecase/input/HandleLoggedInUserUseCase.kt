package com.duberton.application.usecase.input

import com.duberton.application.domain.User
import com.duberton.application.port.output.HandleLoggedInUserPort
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.duberton.application.port.output.UserRepositoryPort

class HandleLoggedInUserUseCase(
    private val userRepositoryPort: UserRepositoryPort,
    private val userCacheRepositoryPort: UserCacheRepositoryPort
) : HandleLoggedInUserPort {

    override fun execute(user: User) {
        val userByEmailCache = user.findUserByEmailCache(userCacheRepositoryPort)
        userByEmailCache?.updateCache(userCacheRepositoryPort) ?: user.saveCache(userCacheRepositoryPort)
    }
}