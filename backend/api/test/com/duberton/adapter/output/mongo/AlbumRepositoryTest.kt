package com.duberton.adapter.output.mongo

import com.duberton.common.dummyObject
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bson.Document
import org.bson.conversions.Bson
import org.junit.Test
import java.time.LocalDate

class AlbumRepositoryTest {

    private val mongoCollection = mockk<MongoCollection<Document>>(relaxed = true)

    private val albumRepository = AlbumRepository(mongoCollection)

    @Test
    fun `given an album, when i save it, then it should be properly persisted`() {
        albumRepository.save(dummyObject())

        verify { mongoCollection.insertOne(any()) }
    }

    @Test
    fun `given an email, when i try to find an albums by it, then it should pass`() {
        every { mongoCollection.find(any<Bson>()) } returns mockk<FindIterable<Document>>(relaxed = true)

        albumRepository.findByEmail("email")

        verify { mongoCollection.find(any<Bson>()) }
    }

    @Test
    fun `given a release date, when i try to find an albums by it, then it should pass`() {
        every { mongoCollection.find(any<Bson>()) } returns mockk<FindIterable<Document>>(relaxed = true)

        albumRepository.findByReleaseDate(LocalDate.now().toString())

        verify { mongoCollection.find(any<Bson>()) }
    }
}