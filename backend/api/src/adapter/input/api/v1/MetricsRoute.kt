package com.duberton.adapter.input.api.v1

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Routing.metricsRoute() {

    val logger = LoggerFactory.getLogger(this::class.java)

    val prometheusMeterRegistry by inject<PrometheusMeterRegistry>()

    route("/v1/metrics") {
        get {
            logger.info("Request performed to the metrics route")
            call.respondText { prometheusMeterRegistry.scrape() }
        }
    }
}