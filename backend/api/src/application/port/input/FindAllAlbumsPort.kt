package com.duberton.application.port.input

import com.duberton.application.domain.Album

interface FindAllAlbumsPort {

    fun execute(email: String, previous: String?, next: String?, limit: Int): List<Album>
}