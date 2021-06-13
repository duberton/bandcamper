package com.duberton.adapter.output.okhttp

import com.duberton.application.port.output.RestClientPort
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory

class BandcampService(private val okHttpClient: OkHttpClient) : RestClientPort {

    private val logger = LoggerFactory.getLogger(BandcampService::class.java)

    override fun get(url: String): Response {
        logger.info("Starting to perform a GET for the following url {}", url)
        val request = Request.Builder().url(url).build()
        return okHttpClient.newCall(request).execute()
    }
}