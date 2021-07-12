package com.duberton.adapter.input.api.v1.config

import com.duberton.application.port.input.FindAllAlbumsPort
import com.duberton.application.port.input.ScrapeAlbumPagePort
import com.duberton.application.port.output.SaveUserPort
import com.duberton.application.usecase.input.FindAllAlbumsUseCase
import com.duberton.application.usecase.input.SaveUserUseCase
import com.duberton.application.usecase.input.ScrapeAlbumPageUseCase
import org.koin.dsl.module

val apiModule = module {
    single<ScrapeAlbumPagePort> { ScrapeAlbumPageUseCase(get(), get()) }
    single<FindAllAlbumsPort> { FindAllAlbumsUseCase(get()) }
    single<SaveUserPort> { SaveUserUseCase(get()) }
}