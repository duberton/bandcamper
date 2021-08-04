package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.output.AlbumRepositoryPort

class FindReleasedAlbumsUseCase(private val albumRepositoryPort: AlbumRepositoryPort) : FindReleasedAlbumsPort {

    override fun find(releaseDate: String): List<Album> {
        return albumRepositoryPort.findByReleaseDate(releaseDate)
    }
}
