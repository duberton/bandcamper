package com.duberton.adapter.output.skraper

import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.application.domain.Album
import com.duberton.application.port.output.RestClientPort
import com.duberton.application.port.output.ScrapeUrlPort
import it.skrape.core.htmlDocument
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SkrapeUrlService(private val restClientPort: RestClientPort) : ScrapeUrlPort {

    override fun execute(album: Album, email: String?): Album {
        val response = restClientPort.get(album.url)
        return response.body?.let {
            val html = htmlDocument(html = it.string())
            val dateElement: Node?
            val documentBody = html.document.body()
            val albumCoverUrl = documentBody.getElementsByClass("popupImage").firstOrNull()?.attr("href")
            val albumCreditsElement = documentBody.getElementsByClass("tralbumData tralbum-credits")
            val releaseElement = albumCreditsElement.first().childNodes()
                .find { node -> node is TextNode && node.text().contains("releases") }
            if (releaseElement == null) {
                dateElement = albumCreditsElement.first().childNodes()
                    .find { node -> node is TextNode && node.text().contains("released") }
                album.isReleased = true
            } else {
                dateElement = releaseElement
                album.isReleased = false
            }
            val releaseDateString = (dateElement as TextNode).text()
            val releaseDate = releaseDateString.replace("released|releases".toRegex(), "").trim()
            val nameSection = documentBody.getElementById("name-section")
            val albumTitle = nameSection.child(0).text()
            val artist = documentBody.getElementById("band-name-location").child(0).text()
            val parsedReleaseDate =
                LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH))
            album.copy(
                artist = artist,
                title = albumTitle,
                releaseDate = parsedReleaseDate,
                email = email,
                albumCoverUrl = albumCoverUrl,
                createdAt = LocalDateTime.now()
            )
        } ?: throw BusinessException("Request for ${album.url} returned an empty body")
    }
}