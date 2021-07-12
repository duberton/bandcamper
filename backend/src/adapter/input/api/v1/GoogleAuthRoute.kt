package com.duberton.adapter.input.api.v1

import com.duberton.adapter.input.api.v1.ext.toDomain
import com.duberton.adapter.input.api.v1.request.UserInfoRequest
import com.duberton.application.port.output.SaveUserPort
import io.ktor.application.call
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Routing.googleAuthRoute(httpClient: HttpClient) {

    val saveUserPort by inject<SaveUserPort>()

    authenticate("auth-oauth-google") {
        get("/login") {
            call.respondRedirect("/callback")
        }

        get("/callback") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
            val userInfoJson = httpClient.get<String>("https://www.googleapis.com/userinfo/v2/me") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${principal?.accessToken}")
                }
            }
            val userInfoRequest = Json.decodeFromString(UserInfoRequest.serializer(), userInfoJson)
            saveUserPort.save(userInfoRequest.toDomain())
            call.respond(HttpStatusCode.Accepted, userInfoRequest)
        }
    }
}