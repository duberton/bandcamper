package com.duberton

import com.duberton.adapter.input.api.v1.albums
import com.duberton.adapter.input.api.v1.config.apiModule
import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.adapter.output.mongo.config.mongoModule
import com.duberton.adapter.output.okhttp.config.okHttpModules
import com.duberton.adapter.output.skraper.config.skraperModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val appConfig = environment.config

    val httpClient = HttpClient(OkHttp)

    install(Sessions) {
        cookie<UserSession>(UserSession::class.java.simpleName)
//        header<UserSession>(UserSession::class.java.simpleName)
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
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                )
            }
            client = httpClient
        }
    }

    install(Koin) {
        modules(listOf(skraperModule, okHttpModules, apiModule, mongoModule(appConfig)))
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }

    routing {
        authenticate("auth-oauth-google") {
            get("login") {
                call.respondRedirect("/callback")
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                println(principal?.accessToken.toString())
                call.sessions.set(UserSession(principal?.accessToken.toString()))
                call.respond(HttpStatusCode.Accepted)
            }
        }
        albums()
        get("/hello") {
            val userSession = call.sessions.get<UserSession>()
            if (userSession != null) {
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                    }
                }
                call.respondText("Hello, ${userInfo.name}!")
            } else {
                call.respondRedirect("/")
            }
        }
    }
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UserSession(val token: String)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class UserInfo(
    val id: String,
    val name: String,
    val givenName: String,
    val familyName: String,
    val picture: String,
    val locale: String
)
