package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.output.AlbumRepositoryPort

class FindAllAlbumsUseCase(private val albumRepositoryPort: AlbumRepositoryPort) : FindAllAlbumsPort {

    override fun execute(email: String, previous: String?, next: String?, limit: Int): List<Album> {
        return albumRepositoryPort.findByEmailWithCursor(email, previous, next, limit)
    }
}