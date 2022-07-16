package com.duberton

import com.duberton.adapter.input.api.v1.albums
import com.duberton.adapter.input.api.v1.authenticateRoute
import com.duberton.adapter.input.api.v1.config.apiModule
import com.duberton.adapter.input.api.v1.error.BusinessException
import com.duberton.adapter.input.api.v1.googleAuthRoute
import com.duberton.adapter.input.api.v1.healthCheckRoute
import com.duberton.adapter.input.api.v1.jwt.Jwt
import com.duberton.adapter.input.api.v1.metricsRoute
import com.duberton.adapter.input.quartz.JobScheduler
import com.duberton.adapter.input.quartz.config.quartzModule
import com.duberton.adapter.output.aws.dynamodb.config.dynamoDBModule
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
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respond
import io.ktor.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@JvmOverloads
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
            urlProvider = { appConfig.property("ktor.oauth.google.urlProvider").getString() }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = appConfig.property("ktor.oauth.google.clientId").getString(),
                    clientSecret = appConfig.property("ktor.oauth.google.clientSecret").getString(),
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

    install(CORS) {
        anyHost()
        allowSameOrigin = true
        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowOrigin)
        CORS.Configuration.CorsSimpleRequestHeaders.forEach { header(it) }
        allowNonSimpleContentTypes = true
        method(HttpMethod.Get)
        method(HttpMethod.Post)
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
                quartzModule,
                dynamoDBModule(appConfig)
            )
        )
    }

    install(MicrometerMetrics) {
        registry = get<PrometheusMeterRegistry>()
        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
            JvmThreadMetrics(),
            FileDescriptorMetrics(),
            UptimeMetrics()
        )
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
    }

    routing {
        albums()
        googleAuthRoute(httpClient)
        authenticateRoute(httpClient)
        healthCheckRoute()
        metricsRoute()
    }

    val isTesting = appConfig.property("ktor.testing").getString().toBoolean()

    if (!isTesting) JobScheduler.start(appConfig)
}