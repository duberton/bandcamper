package com.duberton.adapter.output.okhttp.config

import com.duberton.adapter.output.okhttp.BandcampService
import com.duberton.application.port.output.RestClientPort
import okhttp3.OkHttpClient
import org.koin.dsl.module

val okHttpModules = module {
    single { OkHttpClient().newBuilder().build() }
    single<RestClientPort> { BandcampService(get()) }
}