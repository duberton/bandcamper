package com.duberton.application.usecase.input

import com.duberton.application.domain.User
import com.duberton.application.port.output.SaveUserPort
import com.duberton.application.port.output.UserRepositoryPort
import org.slf4j.LoggerFactory

class SaveUserUseCase(private val userRepositoryPort: UserRepositoryPort) : SaveUserPort {

    private val logger = LoggerFactory.getLogger(SaveUserPort::class.java)

    override fun save(user: User) {
        logger.info("Starting to save an user {}", user.fullName)
        userRepositoryPort.save(user)
        logger.info("Done saving an user {}", user.fullName)
    }
}