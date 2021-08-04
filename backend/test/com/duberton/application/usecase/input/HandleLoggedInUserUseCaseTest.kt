package com.duberton.application.usecase.input

import com.duberton.application.domain.User
import com.duberton.application.port.output.UserCacheRepositoryPort
import com.duberton.common.dummyObject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class HandleLoggedInUserUseCaseTest {

    private val userCacheRepositoryPort = mockk<UserCacheRepositoryPort>(relaxed = true)

    private val handleLoggedInUserUseCase = HandleLoggedInUserUseCase(userCacheRepositoryPort)

    @Test
    fun `given a fresh user that needs to be authed, when i handle it, then it should be successful`() {
        every { userCacheRepositoryPort.findByEmail(any()) } returns null
        val user = dummyObject<User>()

        handleLoggedInUserUseCase.execute(user)

        verify { userCacheRepositoryPort.findByEmail(user.email) }
        verify(exactly = 0) { userCacheRepositoryPort.update(user) }
        verify { userCacheRepositoryPort.save(any()) }
    }

    @Test
    fun `given a non existing user that needs to be authed, when i handle it, then it should be successful`() {
        val user = dummyObject<User>()
        every { userCacheRepositoryPort.findByEmail(any()) } returns user

        handleLoggedInUserUseCase.execute(user)

        verify { userCacheRepositoryPort.findByEmail(user.email) }
        verify { userCacheRepositoryPort.update(any()) }
        verify(exactly = 0) { userCacheRepositoryPort.save(user) }
    }
}