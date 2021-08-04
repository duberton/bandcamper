package com.duberton.adapter.input.quartz.starters

import com.duberton.adapter.input.quartz.jobs.ReleasedAlbumsJob
import io.ktor.config.ApplicationConfig
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.TriggerBuilder
import java.time.Instant
import java.util.Date

const val START_UP_TIME_RELEASED_ALBUM_JOB = 10L

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
            .startAt(Date.from(Instant.now().plusMillis(START_UP_TIME_RELEASED_ALBUM_JOB)))
            .forJob(job)
            .build()

        return Pair(job, trigger)
    }
}