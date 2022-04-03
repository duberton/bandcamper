package com.duberton.adapter.output.okhttp.config

import com.duberton.adapter.output.okhttp.BandcamperApiService
import com.duberton.application.port.output.BandcamperApiPort
import com.duberton.application.port.output.FindReleasedAlbumsPort
import com.duberton.application.port.output.ProcessReleasedAlbumsPort
import com.duberton.application.usecase.input.FindReleasedAlbumsUseCase
import com.duberton.application.usecase.input.ProcessReleasedAlbumsUseCase
import okhttp3.OkHttpClient
import org.koin.dsl.module

val okHttpModules = module {
    single { OkHttpClient().newBuilder().build() }
    single<BandcamperApiPort> { BandcamperApiService(get()) }
    single<FindReleasedAlbumsPort> { FindReleasedAlbumsUseCase(get()) }
    single<ProcessReleasedAlbumsPort> { ProcessReleasedAlbumsUseCase(get(), get()) }
}