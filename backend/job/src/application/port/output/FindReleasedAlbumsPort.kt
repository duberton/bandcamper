package com.duberton.application.port.output

import com.duberton.application.domain.Album

interface FindReleasedAlbumsPort {

    fun find(releaseDate: String): List<Album>
}