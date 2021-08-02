package com.duberton.application.port.output

import com.duberton.application.domain.Album

interface EmailNotificationPort {

    fun sendEmail(album: Album)
}
