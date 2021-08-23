package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface FindAllAlbumsPort {

    fun execute(email: String, cursor: String?, limit: Int): List<Album>
}