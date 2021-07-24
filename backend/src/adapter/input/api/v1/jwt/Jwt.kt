package com.duberton.adapter.input.api.v1.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.duberton.application.domain.User
import java.util.*

object Jwt {

    private const val expireTimeInMs = 1000 * 60 * 5
    private const val issuer = "com.duberton.bandcamper"
    private var secret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(secret)

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