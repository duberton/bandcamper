package com.duberton.adapter.output.okhttp.config

import com.duberton.adapter.output.okhttp.BandcamperApiService
import com.duberton.application.port.output.BandcamperApiPort
import com.duberton.application.port.output.FindReleasedAlbumsPort
import com.duberton.application.port.output.ProcessReleasedAlbumsPort
import com.duberton.application.usecase.input.FindReleasedAlbumsUseCase
import com.duberton.application.usecase.input.ProcessReleasedAlbumsUseCase
import io.ktor.config.ApplicationConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module

fun okHttpModules(applicationConfig: ApplicationConfig) = module {
    single { OkHttpClient().newBuilder().build() }
    single<BandcamperApiPort> { BandcamperApiService(get(), applicationConfig) }
    single<FindReleasedAlbumsPort> { FindReleasedAlbumsUseCase(get()) }
    single<ProcessReleasedAlbumsPort> { ProcessReleasedAlbumsUseCase(get(), get()) }
}