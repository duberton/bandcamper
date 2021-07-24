package com.duberton.adapter.output.aws.ses.config

import com.duberton.adapter.output.aws.ses.SesEmailNotification
import com.duberton.application.port.output.EmailNotificationPort
import org.koin.dsl.module
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient
import java.net.URI

val sesModule = module {
    single { buildSesModule() }
    single<EmailNotificationPort> { SesEmailNotification(get()) }
}

fun buildSesModule(): SesAsyncClient =
    SesAsyncClient.builder().region(Region.US_EAST_1).endpointOverride(URI.create("http://localhost:4566")).build()