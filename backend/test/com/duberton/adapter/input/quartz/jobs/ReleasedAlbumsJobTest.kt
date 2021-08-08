package com.duberton.adapter.input.quartz.jobs

import com.duberton.application.port.input.ProcessReleasedAlbumsPort
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.quartz.JobExecutionContext

class ReleasedAlbumsJobTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create { }

    @get:Rule
    val mockProvider = MockProviderRule.create { mockkClass(it) }

    private val releasedAlbumsJob = ReleasedAlbumsJob()

    @Test
    fun `given a job to process album that were released, when the time is due, then the proper ports should be invoked`() {
        val processReleasedAlbumsPort = declareMock<ProcessReleasedAlbumsPort>()
        every { processReleasedAlbumsPort.execute(any()) } returns Unit
        val jobExecutionContext = mockk<JobExecutionContext>(relaxed = true)

        releasedAlbumsJob.execute(jobExecutionContext)

        verify { processReleasedAlbumsPort.execute(any()) }
    }
}