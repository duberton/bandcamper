package com.duberton.adapter.input.api.jsonapi.response

data class SingleResourceResponse<T : BaseResponse>(val data: ResourceData<T>) {

    data class ResourceData<T : BaseResponse>(
        val attributes: T,
        val id: String? = attributes.id,
        val type: String = attributes.type()
    )
}