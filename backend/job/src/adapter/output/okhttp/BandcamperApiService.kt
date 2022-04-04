package com.duberton.adapter.output.okhttp

import com.duberton.application.port.output.BandcamperApiPort
import io.ktor.config.ApplicationConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory

class BandcamperApiService(private val okHttpClient: OkHttpClient, private val applicationConfig: ApplicationConfig) :
    BandcamperApiPort {

    private val baseUrl = applicationConfig.property("ktor.api.url").getString()

    private val logger = LoggerFactory.getLogger(BandcamperApiService::class.java)

    override fun findAlbumsFromReleaseDate(releaseDate: String): Response {
        val url = "$baseUrl/v1/album/${releaseDate}"
        logger.info("Starting to perform call for the following url {}", url)
        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        logger.info("Done performing call for the following url {}", url)
        return response
    }
}