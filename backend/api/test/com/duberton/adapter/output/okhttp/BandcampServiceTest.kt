package com.duberton.adapter.output.okhttp

import io.mockk.mockk
import io.mockk.verify
import okhttp3.OkHttpClient
import org.junit.Test

class BandcampServiceTest {

    private val okHttpClient = mockk<OkHttpClient>(relaxed = true)

    private val bandcampService = BandcampService(okHttpClient)

    @Test
    fun `given a bandcamp url, when i get it, then it should invoke the client`() {
        bandcampService.get("http://url.com")

        verify { okHttpClient.newCall(any()) }
    }
}