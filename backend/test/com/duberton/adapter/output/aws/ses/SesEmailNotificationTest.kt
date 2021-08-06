package com.duberton.adapter.output.aws.ses

import com.duberton.common.dummyObject
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.SendEmailRequest

class SesEmailNotificationTest {

    private val sesAsyncClient = mockk<SesAsyncClient>(relaxed = true)

    private val sesEmailNotification = SesEmailNotification(sesAsyncClient)

    @Test
    fun `given an album, when i mail its information, it should invoke the proper client`() {
        sesEmailNotification.sendEmail(dummyObject())

        verify { sesAsyncClient.sendEmail(any<SendEmailRequest>()) }
    }
}