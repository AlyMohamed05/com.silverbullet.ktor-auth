package com.silverbullet.auth.routes

import com.silverbullet.auth.AuthRequest
import com.silverbullet.auth.AuthResponse
import com.silverbullet.security.hashing.HashingService
import com.silverbullet.security.hashing.SaltedHash
import com.silverbullet.security.token.TokenClaim
import com.silverbullet.security.token.TokenConfig
import com.silverbullet.security.token.TokenService
import com.silverbullet.user.data.UserDataSource
import com.silverbullet.user.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.signUp() {
    val hashingService: HashingService by inject()
    val userDataSource: UserDataSource by inject()

    post("signup") {
        kotlin.runCatching {
            call.receive<AuthRequest>()
        }.apply {
            onSuccess { authRequest ->
                val areFieldsBlank = authRequest.username.isBlank() || authRequest.password.isBlank()
                val isPasswordShort = authRequest.password.length < 8
                if (areFieldsBlank || isPasswordShort) {
                    call.respond(HttpStatusCode.Conflict, "Password too short")
                    return@post
                }
                val saltedHash = hashingService.generateSaltedHash(authRequest.password)
                val user = User(
                    authRequest.username,
                    password = saltedHash.hash,
                    salt = saltedHash.salt
                )
                val isUserCreated = userDataSource.insertUser(user)
                if (!isUserCreated) {
                    call.respond(HttpStatusCode.Conflict, "Failed to create account")
                    return@post
                }
                call.respond(HttpStatusCode.OK)
            }
            onFailure {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.signIn(tokenConfig: TokenConfig) {
    val hashingService: HashingService by inject()
    val userDataSource: UserDataSource by inject()
    val tokenService: TokenService by inject()

    post("signin") {
        kotlin.runCatching {
            call.receive<AuthRequest>()
        }.apply {
            onSuccess { authRequest ->
                val user = userDataSource.getUserByUsername(authRequest.username)
                if (user == null) {
                    call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                    return@post
                }
                val isPasswordValid = hashingService
                    .verify(
                        value = authRequest.password,
                        saltedHash = SaltedHash(
                            hash = user.password,
                            salt = user.salt
                        )
                    )
                if (!isPasswordValid) {
                    call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
                    return@post
                }
                val token = tokenService
                    .generate(
                        tokenConfig = tokenConfig,
                        TokenClaim(
                            name = "userId",
                            value = user.id.toString()
                        )
                    )
                call.respond(HttpStatusCode.OK, message = AuthResponse(token = token))
            }
            onFailure {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "User Id: $userId")
        }
    }
}