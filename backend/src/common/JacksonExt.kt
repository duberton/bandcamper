package com.duberton.common

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun <T> T.objectToJson(): String =
    jacksonObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule()).writeValueAsString(this)

fun <T> String.jsonToObject(t: Class<T>): T =
    jacksonObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule()).readValue(this, t)