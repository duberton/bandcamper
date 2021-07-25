package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface ProcessReleasedAlbumsPort {

    fun execute(releaseDate: String)
}