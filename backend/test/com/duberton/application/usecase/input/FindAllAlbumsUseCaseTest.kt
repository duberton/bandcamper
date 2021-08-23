package com.duberton.application.usecase.input

import com.duberton.application.port.output.AlbumRepositoryPort
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.time.LocalDateTime

class FindAllAlbumsUseCaseTest {

    private val albumRepositoryPort = mockk<AlbumRepositoryPort>(relaxed = true)

    private val findAllAlbumsUseCase = FindAllAlbumsUseCase(albumRepositoryPort)

    @Test
    fun `given an email, when i try to find all albums that belongs to it, then it should properly do it`() {
        val email = "email"

        findAllAlbumsUseCase.execute(email, LocalDateTime.now().toString(), 10)

        verify { albumRepositoryPort.findByEmailWithCursor(email, any(), any()) }
    }
}