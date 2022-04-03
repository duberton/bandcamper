package com.duberton.application.port.output

import okhttp3.Response

interface BandcamperApiPort {

    fun findAlbumsFromReleaseDate(releaseDate: String): Response
}