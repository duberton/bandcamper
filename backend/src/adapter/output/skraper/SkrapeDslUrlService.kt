package com.duberton.adapter.output.skraper

import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.application.domain.Album
import com.duberton.application.port.output.ScrapeUrlPort
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.and
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.p
import it.skrape.selects.html5.span
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class SkrapeDslUrlService : ScrapeUrlPort {

    override fun execute(album: Album, email: String?): Album {
        return skrape(HttpFetcher) {
            request {
                url = album.url
            }
            extractIt<Album> {
                if (responseStatus.code != 200) throw BusinessException("Request for ${album.url} returned an empty body")
                htmlDocument {
                    it.isReleased = div {
                        withClass = "tralbumData".and("tralbum-credits")
                        findFirst { text.contains("released") }
                    }
                    it.artist = p {
                        withId = "band-name-location"
                        span {
                            withClass = "title"
                            findFirst { text }
                        }
                    }
                    it.title = div {
                        withId = "name-section"
                        h2 {
                            withClass = "trackTitle"
                            findFirst { text }
                        }
                    }
                    it.albumCoverUrl = a {
                        withClass = "popupImage"
                        findFirst { attribute("href") }
                    }
                    it.releaseDate = div {
                        withClass = "tralbumData".and("tralbum-credits")
                        findFirst {
                            val date = Regex(RELEASE_DATE_REGEX).find(text)?.groups?.first()?.value
                            LocalDate.parse(
                                date?.replace(RELEASED_OR_RELEASED_REGEX.toRegex(), "")?.trim(),
                                DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.ENGLISH)
                            ).toString()
                        }
                    }
                }
            }.copy(url = album.url, createdAt = LocalDateTime.now(), email = email)
        }
    }
}

const val RELEASE_DATE_REGEX = "(release[d|s]\\s[a-zA-Z]*\\s\\d*,\\s\\d*)"
const val RELEASED_OR_RELEASED_REGEX = "released|releases"