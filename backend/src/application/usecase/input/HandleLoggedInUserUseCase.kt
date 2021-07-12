package com.duberton.application.usecase.input

import com.duberton.application.domain.User
import com.duberton.application.port.output.HandleLoggedInUserPort
import com.duberton.application.port.output.UserRepositoryPort

class HandleLoggedInUserUseCase(private val userRepositoryPort: UserRepositoryPort) : HandleLoggedInUserPort {

    override fun execute(user: User) {
        val userByEmail = user.findUserByEmail(userRepositoryPort)
        userByEmail?.update(userRepositoryPort) ?: user.save(userRepositoryPort)
    }
}