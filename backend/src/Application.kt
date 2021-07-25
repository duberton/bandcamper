package com.duberton

import com.duberton.adapter.input.api.v1.albums
import com.duberton.adapter.input.api.v1.config.apiModule
import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.adapter.input.api.v1.googleAuthRoute
import com.duberton.adapter.input.api.v1.jwt.Jwt
import com.duberton.adapter.input.quartz.AlbumJobStarter
import com.duberton.adapter.input.quartz.config.quartzModule
import com.duberton.adapter.output.aws.ses.config.sesModule
import com.duberton.adapter.output.mongo.config.mongoModule
import com.duberton.adapter.output.okhttp.config.okHttpModules
import com.duberton.adapter.output.redis.config.redisModule
import com.duberton.adapter.output.skraper.config.skraperModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val appConfig = environment.config

    val httpClient = HttpClient(OkHttp)

    install(Sessions) {
    }

    install(StatusPages) {
        exception<BusinessException> {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("profile", "email")
                )
            }
            client = httpClient
        }

        jwt {

            verifier(Jwt.verifier)
            realm = javaClass.packageName
            validate {
                val emailClaim = it.payload.getClaim("email").asString()
                if (emailClaim != null) JWTPrincipal(it.payload) else null
            }
        }
    }

    install(Koin) {
        modules(
            listOf(
                skraperModule,
                okHttpModules,
                apiModule,
                mongoModule(appConfig),
                redisModule,
                sesModule,
                quartzModule
            )
        )
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }

    routing {
        googleAuthRoute(httpClient)
        albums()
    }

    AlbumJobStarter.start()
}