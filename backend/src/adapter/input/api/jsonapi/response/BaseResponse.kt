package com.duberton.adapter.input.api.jsonapi.response

abstract class BaseResponse(open val id: String?) {

    abstract fun type(): String

}