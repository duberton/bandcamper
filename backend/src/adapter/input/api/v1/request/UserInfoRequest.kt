package com.duberton.adapter.input.api.v1.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val id: String,
    val name: String,
    @SerialName("given_name")
    val givenName: String,
    @SerialName("family_name")
    val familyName: String,
    val picture: String,
    val locale: String,
    val email: String,
    @SerialName("verified_email")
    val verifiedEmail: Boolean
)
