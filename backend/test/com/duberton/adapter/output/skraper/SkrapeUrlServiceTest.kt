package com.duberton.adapter.output.skraper

import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.application.domain.Album
import com.duberton.application.port.output.RestClientPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertThrows
import org.junit.Test

class SkrapeUrlServiceTest {

    private val restClientPort = mockk<RestClientPort>(relaxed = true)

    private val skrapeUrlService = SkrapeUrlService(restClientPort)

    @Test
    fun `given an url that needs to be scraped and parsed, when i do it, then it should be successful`() {
        val email = "email@gmail.com"
        val album = Album(url = "http://url.com")
        val content = this.javaClass.classLoader.getResource("skrape-url-content-released.html")?.readText()
        every { restClientPort.get(any()) } returns Response.Builder()
            .request(Request.Builder().url(album.url).build())
            .message("message")
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(content?.toResponseBody("text/html".toMediaType())).build()

        skrapeUrlService.execute(album, email)

        verify { restClientPort.get(any()) }
    }

    @Test
    fun `given an url of an album that will be released, when i do it, then it should be successful`() {
        val email = "email@gmail.com"
        val album = Album(url = "http://url.com")
        val content = this.javaClass.classLoader.getResource("skrape-url-content-releases.html")?.readText()
        every { restClientPort.get(any()) } returns Response.Builder()
            .request(Request.Builder().url(album.url).build())
            .message("message")
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(content?.toResponseBody("text/html".toMediaType())).build()

        skrapeUrlService.execute(album, email)

        verify { restClientPort.get(any()) }
    }

    @Test
    fun `given an invalid url that returns no body, when i do it, then the response should he handled gracefully`() {
        val email = "email@gmail.com"
        val album = Album(url = "http://url.com")
        every { restClientPort.get(any()) } returns Response.Builder()
            .request(Request.Builder().url(album.url).build())
            .message("message")
            .protocol(Protocol.HTTP_1_1)
            .code(200).build()

        assertThrows(BusinessException::class.java) { skrapeUrlService.execute(album, email) }

        verify { restClientPort.get(any()) }
    }
}