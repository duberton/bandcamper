package com.duberton.application.port.input

interface ProcessReleasedAlbumsPort {

    fun execute(releaseDate: String)
}