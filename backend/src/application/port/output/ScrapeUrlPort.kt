package com.duberton.application.port.output

import com.duberton.application.domain.Album

interface ScrapeUrlPort {

    fun execute(album: Album, email: String?): Album

}