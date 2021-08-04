package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface ScrapeAlbumPagePort {

    fun execute(album: Album, email: String?): Album
}