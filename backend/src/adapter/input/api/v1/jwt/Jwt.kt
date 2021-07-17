package com.duberton.adapter.input.api.v1.oauth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.duberton.application.domain.User
import java.util.*

object Jwt {

    private const val expireTimeInMs = 12_000_00 * 24
    private var secret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(secret)
    private const val issuer = "com.duberton.bandcamper"

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("jwt-auth")
        .withIssuer(issuer)
        .withClaim("name", user.fullName)
        .withClaim("email", user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + expireTimeInMs))
        .sign(algorithm)

}