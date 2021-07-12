package com.duberton.adapter.output.mongo

import com.duberton.adapter.output.mongo.ext.toDocument
import com.duberton.application.domain.User
import com.duberton.application.port.output.UserRepositoryPort
import com.mongodb.MongoClient

class UserRepository(mongoClient: MongoClient) : UserRepositoryPort {

    private val collection = mongoClient.getDatabase("bandcamper").getCollection("user")

    override fun save(user: User) {
        collection.insertOne(user.toDocument())
    }
}