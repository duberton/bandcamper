package com.duberton.adapter.input.api.jsonapi.response

data class SingleResourceResponse<T : BaseResponse>(val entity: ResponseData<T>) {

    data class ResponseData<T : BaseResponse>(val data: Data<T>, val links: Links? = null) {

        data class Data<T : BaseResponse>(
            val attributes: T,
            val id: String? = attributes.id,
            val type: String = attributes.type()
        )

        data class Links(
            val first: String,
            val last: String,
            val prev: String,
            val next: String
        )
    }
}