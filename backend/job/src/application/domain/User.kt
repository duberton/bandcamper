package com.duberton.application.domain

data class User(
    val id: String? = null,
    val googleId: String,
    val fullName: String,
    val pictureUrl: String,
    val country: String,
    val email: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)