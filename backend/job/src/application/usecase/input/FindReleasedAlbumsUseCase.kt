package com.duberton.application.usecase.input

import com.duberton.application.domain.Album
import com.duberton.application.port.output.BandcamperApiPort
import com.duberton.application.port.output.FindReleasedAlbumsPort

class FindReleasedAlbumsUseCase(private val bandcamperApiPort: BandcamperApiPort) : FindReleasedAlbumsPort {

    override fun find(releaseDate: String): List<Album> {
        bandcamperApiPort.findAlbumsFromReleaseDate(releaseDate)
        return emptyList()
    }
}