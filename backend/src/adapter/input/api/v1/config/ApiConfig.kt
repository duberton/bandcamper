package com.duberton.adapter.input.api.v1.config

import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.input.ScrapeAlbumPagePort
import com.duberton.application.port.output.HandleLoggedInUserPort
import com.duberton.application.usecase.input.FindAllAlbumsUseCase
import com.duberton.application.usecase.input.HandleLoggedInUserUseCase
import com.duberton.application.usecase.input.ScrapeAlbumPageUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {
    single<ScrapeAlbumPagePort> { ScrapeAlbumPageUseCase(get(named("dslSkrape")), get(), get()) }
    single<FindAllAlbumsPort> { FindAllAlbumsUseCase(get()) }
    single<HandleLoggedInUserPort> { HandleLoggedInUserUseCase(get()) }
}