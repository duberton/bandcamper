package com.duberton.application.port.output

import com.duberton.application.domain.Album

interface AlbumRepositoryPort {

    fun save(album: Album)

    fun findByEmail(email: String): List<Album>

    fun findByEmailWithCursor(email: String, previous: String?, next: String?, limit: Int): List<Album>

    fun findByReleaseDate(releaseDate: String): List<Album>
}