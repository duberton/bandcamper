package com.duberton.adapter.input.quartz

import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder.newJob
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory

object AlbumJobStarter {

    fun start() {
        val defaultScheduler = StdSchedulerFactory.getDefaultScheduler()

        val job = newJob(ReleasedAlbumsJob::class.java)
            .withIdentity(ReleasedAlbumsJob::class.simpleName, "albums")
            .build()

        val trigger = newTrigger()
            .withIdentity(ReleasedAlbumsJob::class.simpleName, "albums")
            .withSchedule(cronSchedule("* * * * * ?"))
            .startNow()
            .forJob(job)
            .build()

        defaultScheduler.scheduleJob(job, trigger)
        defaultScheduler.start()
    }

}