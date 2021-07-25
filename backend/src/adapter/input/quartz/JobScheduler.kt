package com.duberton.adapter.input.quartz

import com.duberton.adapter.input.quartz.starters.ReleasedAlbumsStarter
import io.ktor.config.*
import org.quartz.impl.StdSchedulerFactory

object JobScheduler {

    fun start(applicationConfig: ApplicationConfig) {
        val defaultScheduler = StdSchedulerFactory.getDefaultScheduler()

        val releasedAlbumsStarter = ReleasedAlbumsStarter(applicationConfig).execute()
        defaultScheduler.scheduleJob(releasedAlbumsStarter.first, releasedAlbumsStarter.second)

        defaultScheduler.start()
    }

}