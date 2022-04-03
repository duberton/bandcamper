package com.duberton.adapter.output.aws.dynamodb.config

import com.duberton.adapter.output.aws.dynamodb.AlbumRepository
import com.duberton.application.port.output.AlbumRepositoryPort
import io.ktor.config.ApplicationConfig
import java.net.URI
import org.koin.core.qualifier.named
import org.koin.dsl.module
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

fun dynamoDBModule(applicationConfig: ApplicationConfig) = module {
    single { buildDynamoDBModule(applicationConfig) }
    single<AlbumRepositoryPort>(named(DYNAMODB_ALBUM_REPO)) {
        AlbumRepository(
            get(), applicationConfig.property("ktor.aws.dynamodb.table").getString()
        )
    }
}

fun buildDynamoDBModule(applicationConfig: ApplicationConfig): DynamoDbAsyncClient {
    val dynamoDBHost = applicationConfig.property("ktor.aws.dynamodb.host").getString()
    val dynamoDBPort = applicationConfig.property("ktor.aws.dynamodb.port").getString()
    return DynamoDbAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("abc", "abc")))
        .region(Region.US_EAST_1).endpointOverride(URI.create("http://$dynamoDBHost:$dynamoDBPort")).build()
}

const val DYNAMODB_ALBUM_REPO = "dynamoDBAlbumRepository"