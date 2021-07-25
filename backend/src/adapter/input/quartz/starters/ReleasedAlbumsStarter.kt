package com.duberton.adapter.input.quartz.starters

import com.duberton.adapter.input.quartz.jobs.ReleasedAlbumsJob
import io.ktor.config.*
import org.quartz.*
import java.time.Instant
import java.util.*

class ReleasedAlbumsStarter(private val applicationConfig: ApplicationConfig) {

    fun execute(): Pair<JobDetail, CronTrigger> {
        val releasedAlbumJobCron = applicationConfig.property("ktor.quartz.releasedAlbumJob.cron").getString()
        val releasedAlbumJobGroup = applicationConfig.property("ktor.quartz.releasedAlbumJob.group").getString()

        val job = JobBuilder.newJob(ReleasedAlbumsJob::class.java)
            .withIdentity(ReleasedAlbumsJob::class.simpleName, releasedAlbumJobGroup)
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(ReleasedAlbumsJob::class.simpleName, releasedAlbumJobGroup)
            .withSchedule(CronScheduleBuilder.cronSchedule(releasedAlbumJobCron))
            .startAt(Date.from(Instant.now().plusMillis(10L)))
            .forJob(job)
            .build()

        return Pair(job, trigger)
    }
}