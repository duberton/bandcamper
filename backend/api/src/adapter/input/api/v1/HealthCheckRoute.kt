package com.duberton.adapter.input.api.v1

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.healthCheckRoute() {

    route("/v1/health") {
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}