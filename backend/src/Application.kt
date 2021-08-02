package com.duberton

import com.duberton.adapter.input.api.v1.albums
import com.duberton.adapter.input.api.v1.config.apiModule
import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.adapter.input.api.v1.googleAuthRoute
import com.duberton.adapter.input.api.v1.healthCheckRoute
import com.duberton.adapter.input.api.v1.jwt.Jwt
import com.duberton.adapter.input.quartz.JobScheduler
import com.duberton.adapter.input.quartz.config.quartzModule
import com.duberton.adapter.output.aws.ses.config.sesModule
import com.duberton.adapter.output.mongo.config.mongoModule
import com.duberton.adapter.output.okhttp.config.okHttpModules
import com.duberton.adapter.output.redis.config.redisModule
import com.duberton.adapter.output.skraper.config.skraperModule
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.OAuthServerSettings
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.auth.oauth
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.routing
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val appConfig = environment.config

    val httpClient = HttpClient(OkHttp)

    install(StatusPages) {
        exception<BusinessException> {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { appConfig.property("oauth.google.urlProvider").getString() }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = appConfig.property("oauth.google.clientId").getString(),
                    clientSecret = appConfig.property("oauth.google.clientSecret").getString(),
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
                redisModule(appConfig),
                sesModule(appConfig),
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
        healthCheckRoute()
    }

    JobScheduler.start(appConfig)
}
