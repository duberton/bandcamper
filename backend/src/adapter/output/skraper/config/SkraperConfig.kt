package com.duberton.adapter.output.skraper.config

import com.duberton.adapter.output.skraper.SkrapeUrlService
import com.duberton.application.port.output.ScrapeUrlPort
import org.koin.dsl.module

val skraperModule = module {
    single<ScrapeUrlPort> { SkrapeUrlService(get()) }
}
