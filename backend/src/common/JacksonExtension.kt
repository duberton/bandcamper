package com.duberton.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun <T> T.objectToJson(): String = jacksonObjectMapper().writeValueAsString(this)

fun <T> String.jsonToObject(t: Class<T>): T = jacksonObjectMapper().readValue(this, t)