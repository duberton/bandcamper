package com.duberton.adapter.output.mongo

import com.duberton.adapter.output.mongo.ext.toDocument
import com.duberton.adapter.output.mongo.ext.toUserDomain
import com.duberton.application.domain.User
import com.duberton.application.port.output.UserRepositoryPort
import com.mongodb.MongoClient
import org.bson.Document

class UserRepository(mongoClient: MongoClient) : UserRepositoryPort {

    private val collection = mongoClient.getDatabase("bandcamper").getCollection("user")

    override fun save(user: User) {
        collection.insertOne(user.toDocument())
    }

    override fun update(user: User) {
        collection.updateOne(Document().append("email", user.email), user.toDocument())
    }

    override fun findByEmail(email: String): User? {
        return collection.find(Document().append("email", email)).firstOrNull()?.toUserDomain()
    }
}