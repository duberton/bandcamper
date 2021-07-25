package com.duberton.application.usecase.input

import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import org.slf4j.LoggerFactory

class ProcessReleasedAlbumsUseCase(private val findReleasedAlbumsPort: FindReleasedAlbumsPort) :
    ProcessReleasedAlbumsPort {

    private val logger = LoggerFactory.getLogger(ProcessReleasedAlbumsUseCase::class.java)

    override fun execute(releaseDate: String) {
        val albums = findReleasedAlbumsPort.find(releaseDate)
        logger.info("Albums found {}", albums.size)
    }
}