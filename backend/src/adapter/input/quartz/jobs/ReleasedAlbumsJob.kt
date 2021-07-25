package com.duberton.adapter.input.quartz.jobs

import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.time.LocalDate

@OptIn(KoinApiExtension::class)
class ReleasedAlbumsJob : Job, KoinComponent {

    private val processReleasedAlbumsPort by inject<ProcessReleasedAlbumsPort>()

    override fun execute(context: JobExecutionContext?) {
        val currentDay = LocalDate.now()
        processReleasedAlbumsPort.execute(currentDay.toString())
    }
}