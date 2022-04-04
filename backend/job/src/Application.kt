package com.duberton

import com.duberton.adapter.output.aws.ses.config.sesModule
import com.duberton.adapter.output.okhttp.config.okHttpModules
import com.duberton.application.port.output.EmailNotificationPort
import com.duberton.application.port.output.FindReleasedAlbumsPort
import io.ktor.application.Application
import io.ktor.application.install
import java.time.LocalDate
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.get

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val appConfig = environment.config

    install(Koin) {
        modules(listOf(okHttpModules(appConfig), sesModule(appConfig)))
    }

    val findReleasedAlbumsPort = get<FindReleasedAlbumsPort>()
    val albums = findReleasedAlbumsPort.find(LocalDate.now().toString())

    val emailNotificationPort = get<EmailNotificationPort>()
    albums.forEach { emailNotificationPort.sendEmail(it) }
}