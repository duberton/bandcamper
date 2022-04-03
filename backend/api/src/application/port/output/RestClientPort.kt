package com.duberton.application.port.output

import okhttp3.Response

interface RestClientPort {

    fun get(url: String): Response
}