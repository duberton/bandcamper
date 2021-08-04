package com.duberton.adapter.output.aws.ses

import com.duberton.application.domain.Album
import com.duberton.application.port.output.EmailNotificationPort
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.Body
import software.amazon.awssdk.services.ses.model.Content
import software.amazon.awssdk.services.ses.model.Destination
import software.amazon.awssdk.services.ses.model.Message
import software.amazon.awssdk.services.ses.model.SendEmailRequest

class SesEmailNotification(private val sesAsyncClient: SesAsyncClient) : EmailNotificationPort {

    override fun sendEmail(album: Album) {
        val sendEmailRequest = SendEmailRequest.builder().source("duberton@gmail.com")
            .replyToAddresses("duberton@gmail.com")
            .destination(Destination.builder().toAddresses(album.email).build())
            .message(
                Message.builder()
                    .subject(Content.builder().charset(Charsets.UTF_8.name()).data("subject").build())
                    .body(
                        Body.builder().text(Content.builder().charset(Charsets.UTF_8.name()).data("body").build())
                            .build()
                    ).build()
            ).build()
        sesAsyncClient.sendEmail(sendEmailRequest)
    }
}