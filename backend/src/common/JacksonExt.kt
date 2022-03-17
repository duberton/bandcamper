package com.duberton.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun <T> T.objectToJson(): String = jacksonObjectMapper().writeValueAsString(this)

inline fun <reified T> String.jsonToObject(): T = jacksonObjectMapper().readValue(this, T::class.java)