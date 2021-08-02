package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface FindReleasedAlbumsPort {

    fun find(releaseDate: String): List<Album>
}
