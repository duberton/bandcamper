package com.duberton.adapter.output.aws.ses.config

import com.duberton.adapter.output.aws.ses.SesEmailNotification
import com.duberton.application.port.output.EmailNotificationPort
import io.ktor.config.*
import org.koin.dsl.module
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient
import java.net.URI

fun sesModule(applicationConfig: ApplicationConfig) = module {
    single { buildSesModule(applicationConfig) }
    single<EmailNotificationPort> { SesEmailNotification(get()) }
}

fun buildSesModule(applicationConfig: ApplicationConfig): SesAsyncClient {
    val sesHost = applicationConfig.property("ktor.aws.ses.host").getString()
    val sesPort = applicationConfig.property("ktor.aws.ses.port").getString()
    return SesAsyncClient.builder().region(Region.US_EAST_1).endpointOverride(URI.create("http://$sesHost:$sesPort"))
        .build()
}
