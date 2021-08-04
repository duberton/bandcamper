package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.output.AlbumRepositoryPort

class FindAllAlbumsUseCase(private val albumRepositoryPort: AlbumRepositoryPort) : FindAllAlbumsPort {

    override fun execute(email: String): List<Album> {
        return albumRepositoryPort.findByEmail(email)
    }
}