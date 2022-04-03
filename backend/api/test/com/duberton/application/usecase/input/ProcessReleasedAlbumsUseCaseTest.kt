package com.duberton.application.usecase.input

import com.duberton.application.port.input.FindReleasedAlbumsPort
import com.duberton.application.port.output.EmailNotificationPort
import com.duberton.common.dummyObject
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.time.LocalDate

class ProcessReleasedAlbumsUseCaseTest {

    private val findReleasedAlbumsPort = mockk<FindReleasedAlbumsPort>(relaxed = true)
    private val emailNotificationPort = mockk<EmailNotificationPort>(relaxed = true)

    private val processReleasedAlbumsUseCase =
        ProcessReleasedAlbumsUseCase(findReleasedAlbumsPort, emailNotificationPort)

    @Test
    fun `given a release date, when i process all the albums from it, then it should be a success`() {
        every { findReleasedAlbumsPort.find(any()) } returns listOf(dummyObject(), dummyObject())

        processReleasedAlbumsUseCase.execute(LocalDate.now().toString())

        verify { findReleasedAlbumsPort.find(any()) }
        verify(exactly = 2) { emailNotificationPort.sendEmail(any()) }
    }

    @Test
    fun `given a release date with no albums for that day, when i process it, then it shouldn't send out emails`() {
        every { findReleasedAlbumsPort.find(any()) } returns emptyList()

        processReleasedAlbumsUseCase.execute(LocalDate.now().toString())

        verify { findReleasedAlbumsPort.find(any()) }
        verify { emailNotificationPort wasNot called }
    }
}