package com.duberton.application.usecase.input

import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.application.domain.Album
import com.duberton.application.port.input.ScrapeAlbumPagePort
import com.duberton.application.port.output.AlbumRepositoryPort
import com.duberton.application.port.output.RestClientPort
import it.skrape.core.htmlDocument
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

class ScrapeAlbumPageUseCase(
    private val restClientPort: RestClientPort,
    private val albumRepositoryPort: AlbumRepositoryPort
) : ScrapeAlbumPagePort {

    override fun execute(album: Album, email: String?) {
        val response = restClientPort.get(album.url)
        response.body?.let {
            val html = htmlDocument(html = it.string())
            val dateElement: Node?
            val documentBody = html.document.body()
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
            val releaseDate = if (album.isReleased!!) releaseDateString.replace("released", "")
                .trim() else releaseDateString.replace("releases", "").trim()
            val nameSection = documentBody.getElementById("name-section")
            val albumTitle = nameSection.child(0).text()
            val artist = documentBody.getElementById("band-name-location").child(0).text()
            val parsedReleaseDate = LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
            album.releaseDate = parsedReleaseDate.toString()
            album.artist = artist
            album.title = albumTitle
            album.email = email
            albumRepositoryPort.save(album)
        } ?: throw BusinessException("Empty body")

    }
}