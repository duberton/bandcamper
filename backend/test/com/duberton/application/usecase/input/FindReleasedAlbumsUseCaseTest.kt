package com.duberton.application.usecase.input

import com.duberton.application.port.output.AlbumRepositoryPort
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test
import java.time.LocalDate

class FindReleasedAlbumsUseCaseTest {

    private val albumRepositoryPort = mockk<AlbumRepositoryPort>(relaxed = true)

    private val findReleasedAlbumsUseCase = FindReleasedAlbumsUseCase(albumRepositoryPort)

    @Test
    fun `given a release date, when i try to find albums off this release date, then it should be successful`() {
        findReleasedAlbumsUseCase.find(LocalDate.now().toString())

        coVerify { albumRepositoryPort.findByReleaseDate(any()) }
    }
}
