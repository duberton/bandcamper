package com.duberton.adapter.input.api.jsonapi.response

data class ManyResourcesResponse<T : BaseResponse>(val data: List<Data<T>>, val cursors: Cursors) {

    data class Data<T : BaseResponse>(
        val attributes: T,
        val id: String? = attributes.id,
        val type: String = attributes.type()
    )

    data class Cursors(
        val previous: String?,
        val next: String?
    )
}