package com.duberton.adapter.input.quartz.config

import com.duberton.adapter.output.aws.dynamodb.config.DYNAMODB_ALBUM_REPO
import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import com.duberton.application.usecase.input.FindReleasedAlbumsUseCase
import com.duberton.application.usecase.input.ProcessReleasedAlbumsUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val quartzModule = module {
    single<FindReleasedAlbumsPort> { FindReleasedAlbumsUseCase(get(named(DYNAMODB_ALBUM_REPO))) }
    single<ProcessReleasedAlbumsPort> { ProcessReleasedAlbumsUseCase(get(), get()) }
}