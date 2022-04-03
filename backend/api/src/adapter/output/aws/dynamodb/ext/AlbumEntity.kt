package com.duberton.adapter.output.aws.dynamodb.ext

import com.duberton.application.domain.Album
import com.duberton.common.jsonToObject
import com.duberton.common.objectToJson
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

fun Album.toDynamoDBItem() = mapOf<String, AttributeValue>(
    "id" to AttributeValue.builder().s(id).build(),
    "url" to AttributeValue.builder().s(url).build(),
    "artist" to AttributeValue.builder().s(artist).build(),
    "title" to AttributeValue.builder().s(title).build(),
    "email" to AttributeValue.builder().s(email).build(),
    "releaseDate" to AttributeValue.builder().s(releaseDate?.toString()).build(),
    "createdAt" to AttributeValue.builder().s(createdAt.toString()).build(),
    "data" to AttributeValue.builder().s(objectToJson()).build()
)

fun String.fromDynamoDBToDomain() = jsonToObject(Album::class.java)