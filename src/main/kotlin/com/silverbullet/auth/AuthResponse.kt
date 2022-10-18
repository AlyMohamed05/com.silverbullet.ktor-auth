package com.silverbullet.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
