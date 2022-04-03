package com.duberton.application.port.output

interface ProcessReleasedAlbumsPort {

    fun execute(releaseDate: String)
}