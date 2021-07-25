package com.duberton.adapter.input.api.v1

import com.duberton.adapter.input.api.v1.ext.toDomain
import com.duberton.adapter.input.api.v1.ext.toResponse
import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import com.duberton.application.port.input.ScrapeAlbumPagePort
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Routing.albums() {

    val scrapeAlbumPagePort by inject<ScrapeAlbumPagePort>()
    val findAllAlbumsPort by inject<FindAllAlbumsPort>()
    val processReleasedAlbumsPort by inject<ProcessReleasedAlbumsPort>()

    val logger = LoggerFactory.getLogger(this::class.java)

    route("/v1/album") {
        authenticate {
            post {
                val albumRequest = call.receive<AlbumRequest>()
                logger.info("Starting to process the creation of an album for the following url {}", albumRequest.url)
                val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                val scrapedAlbum = scrapeAlbumPagePort.execute(albumRequest.toDomain(), email)
                call.respond(HttpStatusCode.Created, scrapedAlbum.toResponse())
                logger.info("Done processing the creation of the album {}", albumRequest.url)
            }
            get {
                val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                logger.info("Starting to find all the albums that belongs to {}", email)
                email?.let {
                    val albums = findAllAlbumsPort.execute(email)
                    call.respond(HttpStatusCode.OK, albums.map { it.toResponse() })
                    logger.info("Done responding to the find all the albums call")
                } ?: call.respond(HttpStatusCode.NotFound)
            }
            get("/{releaseDate}") {
                val releaseDate = call.parameters["releaseDate"]!!
                processReleasedAlbumsPort.execute(releaseDate)
            }
        }
    }
}