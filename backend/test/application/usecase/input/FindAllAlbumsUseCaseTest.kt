package application.usecase.input

import com.duberton.application.port.output.AlbumRepositoryPort
import com.duberton.application.usecase.input.FindAllAlbumsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class FindAllAlbumsUseCaseTest {

    private val albumRepositoryPort = mockk<AlbumRepositoryPort>()

    private val findAllAlbumsUseCase = FindAllAlbumsUseCase(albumRepositoryPort)

    @Test
    fun `given the need to find all the albums from an email, when i do it, then it should be successful`() {
        every { albumRepositoryPort.findByEmail(any()) } returns emptyList()

        findAllAlbumsUseCase.execute("email")

        verify { albumRepositoryPort.findByEmail(any()) }
    }
}
