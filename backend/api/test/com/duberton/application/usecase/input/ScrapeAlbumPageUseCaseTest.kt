package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.output.AlbumRepositoryPort
import com.duberton.application.port.output.EmailNotificationPort
import com.duberton.application.port.output.ScrapeUrlPort
import com.duberton.common.dummyObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class ScrapeAlbumPageUseCaseTest {

    private val scrapeUrlPort = mockk<ScrapeUrlPort>(relaxed = true)
    private val albumRepositoryPort = mockk<AlbumRepositoryPort>(relaxed = true)
    private val emailNotificationPort = mockk<EmailNotificationPort>(relaxed = true)

    private val scrapeAlbumPageUseCase =
        ScrapeAlbumPageUseCase(scrapeUrlPort, albumRepositoryPort, emailNotificationPort)

    @Test
    fun `given an email, when i try to find all albums that belongs to it, then it should properly do it`() {
        val album = dummyObject<Album>()
        val email = "email"

        scrapeAlbumPageUseCase.execute(album, email)

        verify { scrapeUrlPort.execute(any(), any()) }
        verify { albumRepositoryPort.save(any()) }
        verify { emailNotificationPort.sendEmail(any()) }
    }
}