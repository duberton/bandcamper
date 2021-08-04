package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.input.ScrapeAlbumPagePort
import com.duberton.application.port.output.AlbumRepositoryPort
import com.duberton.application.port.output.EmailNotificationPort
import com.duberton.application.port.output.ScrapeUrlPort

class ScrapeAlbumPageUseCase(
    private val scrapeUrlPort: ScrapeUrlPort,
    private val albumRepositoryPort: AlbumRepositoryPort,
    private val emailNotificationPort: EmailNotificationPort
) : ScrapeAlbumPagePort {

    override fun execute(album: Album, email: String?): Album {
        val processedAlbum = scrapeUrlPort.execute(album, email)
        albumRepositoryPort.save(processedAlbum)
        emailNotificationPort.sendEmail(album)
        return processedAlbum
    }
}