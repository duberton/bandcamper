package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface FindAllAlbumsPort {

    fun execute(): List<Album>
}