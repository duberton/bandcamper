package com.duberton.adapter.input.api.v1

import com.duberton.adapter.input.api.v1.ext.toDomain
import com.duberton.adapter.input.api.v1.ext.toManyResponse
import com.duberton.adapter.input.api.v1.ext.toSingleResponse
import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.input.ScrapeAlbumPagePort
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Routing.albums() {

    val scrapeAlbumPagePort by inject<ScrapeAlbumPagePort>()
    val findAllAlbumsPort by inject<FindAllAlbumsPort>()

    val logger = LoggerFactory.getLogger(this::class.java)

    route("/v1/album") {
        authenticate {
            post {
                val albumRequest = call.receive<AlbumRequest>()
                logger.info("Starting to process the creation of an album for the following url {}", albumRequest.url)
                val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                val scrapedAlbum = scrapeAlbumPagePort.execute(albumRequest.toDomain(), email)
                call.respond(HttpStatusCode.Created, scrapedAlbum.toSingleResponse())
                logger.info("Done processing the creation of the following album url {}", albumRequest.url)
            }
            get {
                val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                val previous = call.request.queryParameters["previous"]
                val next = call.request.queryParameters["next"]
                val limit = call.request.queryParameters["limit"]?.toInt() ?: 10
                logger.info("Starting to find all the albums that belongs to {}", email)
                email?.let {
                    val albums = findAllAlbumsPort.execute(email, previous, next, limit)
                    val albumsResponse = albums.toManyResponse(previous, next, limit)
                    call.respond(HttpStatusCode.OK, albumsResponse)
                    logger.info("Done responding to the find all the albums call")
                } ?: call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}