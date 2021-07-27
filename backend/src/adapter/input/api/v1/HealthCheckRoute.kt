package com.duberton.adapter.input.api.v1

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*


fun Routing.healthCheckRoute() {

    route("/v1/health") {
        get {
            call.respond(HttpStatusCode.OK)
        }
    }
}