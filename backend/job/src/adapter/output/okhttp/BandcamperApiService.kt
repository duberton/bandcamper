package com.duberton.adapter.output.okhttp

import com.duberton.application.port.output.BandcamperApiPort
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory

class BandcamperApiService(private val okHttpClient: OkHttpClient) : BandcamperApiPort {

    private val logger = LoggerFactory.getLogger(BandcamperApiService::class.java)

    override fun findAlbumsFromReleaseDate(releaseDate: String): Response {
        val request = Request.Builder().url("http://default.bandcamper-api/v1/album/${releaseDate}").build()
        val response = okHttpClient.newCall(request).execute()
        return response
    }
}