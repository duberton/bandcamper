package com.duberton.adapter.output.skraper

import com.duberton.application.domain.Album
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.okXml
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import it.skrape.core.htmlDocument
import org.junit.BeforeClass
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.test.assertEquals

class SkrapeDslUrlServiceItTest {

    private val skrapeUrlService = SkrapeDslUrlService()

    companion object {

        private val wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicPort())

        @BeforeClass
        @JvmStatic
        fun setup() {
            wireMockServer.start()
        }
    }

    @Test
    fun `given an url from an album that has already been released, when i fetch it, then it should pass`() {
        val url = "http://localhost:${wireMockServer.port()}/released"
        wireMockServer.stubFor(
            WireMock.get(urlPathEqualTo("/released")).willReturn(okXml(pastReleaseDocument).withStatus(200))
        )

        val album = skrapeUrlService.execute(Album(url = url), "mail")

        assertEquals(TRACK_TITLE, album.title)
        assertEquals(ARTIST, album.artist)
        assertEquals(IMAGE_URL, album.albumCoverUrl)
        assertEquals(true, album.isReleased)
    }

    @Test
    fun `given an url from an album that has not been released, when i fetch it, then it should pass`() {
        val url = "http://localhost:${wireMockServer.port()}/released"
        wireMockServer.stubFor(
            WireMock.get(urlPathEqualTo("/released")).willReturn(okXml(futureReleaseDocument).withStatus(200))
        )

        val album = skrapeUrlService.execute(Album(url = url), "mail")

        assertEquals(TRACK_TITLE, album.title)
        assertEquals(ARTIST, album.artist)
        assertEquals(IMAGE_URL, album.albumCoverUrl)
        assertEquals(false, album.isReleased)
    }
}

const val TRACK_TITLE = "track title"
const val ARTIST = "artist"
const val IMAGE_URL = "http://cover.url"
val futureReleaseDate: String =
    LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH))
val pastReleaseDate: String =
    LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH))

val futureReleaseDocument by lazy {
    htmlDocument(
        """<html>
            <body>
                <div>
                    <a class="popupImage" href="$IMAGE_URL"></a>
                    <div class="tralbumData tralbum-credits">releases $futureReleaseDate</div>
                    <div id="name-section">
                        <h2 class="trackTitle">$TRACK_TITLE</h2>
                    </div>
                    <p id="band-name-location">
                        <span class="title">$ARTIST</span>
                    </p>
                </div>
            </body>
        </html>"""
    ).toString()
}

val pastReleaseDocument by lazy {
    htmlDocument(
        """<html>
            <body>
                <div>
                    <a class="popupImage" href="$IMAGE_URL"></a>
                    <div class="tralbumData tralbum-credits">released $pastReleaseDate</div>
                    <div id="name-section">
                        <h2 class="trackTitle">$TRACK_TITLE</h2>
                    </div>
                    <p id="band-name-location">
                        <span class="title">$ARTIST</span>
                    </p>
                </div>
            </body>
        </html>"""
    ).toString()
}