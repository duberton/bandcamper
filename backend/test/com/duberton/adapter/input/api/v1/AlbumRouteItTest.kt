package com.duberton.adapter.input.api.v1

import com.duberton.adapter.input.api.v1.jwt.Jwt
import com.duberton.adapter.input.api.v1.request.AlbumRequest
import com.duberton.adapter.input.api.v1.response.AlbumResponse
import com.duberton.common.dummyObject
import com.duberton.common.jsonToObject
import com.duberton.common.objectToJson
import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AlbumRouteItTest {

    private val albumApiRoot = "/v1/album"

    private val testEnvironment = createTestEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
    }

    @Test
    fun `given a call to create an album, when i perform it, then it should return success`() {
        withApplication(testEnvironment) {
            val url = "https://humanimpact.bandcamp.com/album/ep01"
            with(
                handleRequest(HttpMethod.Post, albumApiRoot) {
                    addHeader(HttpHeaders.Authorization, "Bearer ${Jwt.generateToken(dummyObject())}")
                    addHeader(HttpHeaders.ContentType, "application/json")
                    setBody(AlbumRequest(url = url).objectToJson())
                }
            ) {
                val response = response.content?.jsonToObject(AlbumResponse::class.java)
                assertEquals(url, response?.url)
                assertNotNull(response?.albumCoverUrl)
                assertNotNull(response?.artist)
                assertNotNull(response?.createdAt)
                assertNotNull(response?.title)
                assertNotNull(response?.releaseDate)
            }
        }
    }

    @Test
    fun `given a call to get all the albums, when i perform it, then it should return success`() {
        withApplication(testEnvironment) {
            handleRequest(HttpMethod.Get, albumApiRoot) {
                addHeader(HttpHeaders.Authorization, "Bearer ${Jwt.generateToken(dummyObject())}")
            }
        }
    }
}