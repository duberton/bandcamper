package com.duberton.application.port.output

import com.duberton.application.domain.Album

interface AlbumRepositoryPort {

    fun save(album: Album)

    fun findByEmail(email: String): List<Album>

}