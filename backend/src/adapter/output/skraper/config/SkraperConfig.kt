package com.duberton.adapter.output.skraper.config

import com.duberton.adapter.output.skraper.SkrapeDslUrlService
import com.duberton.adapter.output.skraper.SkrapeUrlService
import com.duberton.application.port.output.ScrapeUrlPort
import org.koin.core.qualifier.named
import org.koin.dsl.module

val skraperModule = module {
    single<ScrapeUrlPort>(named("standardSkrape")) { SkrapeUrlService(get()) }
    single<ScrapeUrlPort>(named("dslSkrape")) { SkrapeDslUrlService() }
}