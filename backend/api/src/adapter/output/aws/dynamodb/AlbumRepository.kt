package com.duberton.adapter.output.aws.dynamodb

import com.duberton.adapter.output.aws.dynamodb.ext.fromDynamoDBToDomain
import com.duberton.adapter.output.aws.dynamodb.ext.toDynamoDBItem
import com.duberton.application.domain.Album
import com.duberton.application.port.output.AlbumRepositoryPort
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

class AlbumRepository(
    private val dynamoDbClient: DynamoDbClient, private val tableName: String
) : AlbumRepositoryPort {

    private val releaseIndexName = "releaseDateIndex"

    override fun save(album: Album) {
        dynamoDbClient.putItem {
            it.tableName(tableName)
            it.item(album.toDynamoDBItem())
        }
    }

    override fun findByEmail(email: String) = dynamoDbClient.query {
        it.tableName(tableName)
        it.keyConditionExpression("email = :email")
        it.expressionAttributeValues(mapOf("email" to AttributeValue.builder().s(email).build()))
    }.items().mapNotNull { it["data"]?.s()?.fromDynamoDBToDomain() }

    override fun findByEmailWithCursor(email: String, previous: String?, next: String?, limit: Int): List<Album> {
        val isDescending = previous != null
        val cursor = when {
            previous != null -> previous
            next != null -> next
            else -> null
        }
        return dynamoDbClient.query {
            it.tableName(tableName)
            it.keyConditionExpression(defineConditionExpression(previous, next))
            it.expressionAttributeValues(defineExpressionValues(previous, next, email, cursor))
            it.scanIndexForward(!isDescending)
            it.limit(limit + 1)
        }.items().mapNotNull { it["data"]?.s()?.fromDynamoDBToDomain() }
    }

    override fun findByReleaseDate(releaseDate: String): List<Album> {
        return dynamoDbClient.query {
            it.tableName(tableName)
            it.indexName(releaseIndexName)
            it.keyConditionExpression("releaseDate = :releaseDate")
            it.expressionAttributeValues(mapOf(":releaseDate" to AttributeValue.builder().s(releaseDate).build()))
        }.items().mapNotNull { it["data"]?.s()?.fromDynamoDBToDomain() }
    }

    private fun defineExpressionValues(previous: String?, next: String?, email: String, cursor: String?) = when {
        previous != null || next != null -> mapOf(
            ":email" to AttributeValue.builder().s(email).build(),
            ":createdAt" to AttributeValue.builder().s(cursor).build()
        )
        else -> mapOf(
            ":email" to AttributeValue.builder().s(email).build()
        )
    }

    private fun defineConditionExpression(previous: String?, next: String?) = when {
        previous != null -> "email = :email and createdAt < :createdAt"
        next != null -> "email = :email and createdAt > :createdAt"
        else -> "email = :email"
    }
}