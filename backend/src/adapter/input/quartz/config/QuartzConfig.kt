package com.duberton.adapter.input.quartz.config

import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import com.duberton.application.usecase.input.FindReleasedAlbumsUseCase
import com.duberton.application.usecase.input.ProcessReleasedAlbumsUseCase
import org.koin.dsl.module

val quartzModule = module {
    single<FindReleasedAlbumsPort> { FindReleasedAlbumsUseCase(get()) }
    single<ProcessReleasedAlbumsPort> { ProcessReleasedAlbumsUseCase(get(), get()) }
}