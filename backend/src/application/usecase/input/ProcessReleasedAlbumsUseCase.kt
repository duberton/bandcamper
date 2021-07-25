package com.duberton.application.usecase.input

import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import com.duberton.application.port.output.EmailNotificationPort
import org.slf4j.LoggerFactory

class ProcessReleasedAlbumsUseCase(
    private val findReleasedAlbumsPort: FindReleasedAlbumsPort,
    private val emailNotificationPort: EmailNotificationPort
) : ProcessReleasedAlbumsPort {

    private val logger = LoggerFactory.getLogger(ProcessReleasedAlbumsUseCase::class.java)

    override fun execute(releaseDate: String) {
        logger.info("Starting to process all the albums that were release today {}", releaseDate)
        val albums = findReleasedAlbumsPort.find(releaseDate)
        albums.forEach { emailNotificationPort.sendEmail(it) }
        logger.info("Done processing albums that were release today {}", releaseDate)
    }
}