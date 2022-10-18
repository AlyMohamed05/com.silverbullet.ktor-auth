package com.silverbullet

import io.ktor.server.application.*
import com.silverbullet.plugins.*
import com.silverbullet.security.token.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 31L * 24L * 60L * 60L * 1000L,
        secret = System.getenv("JWT_SECRET")
    )

    configureKoin()
    configureMonitoring()
    configureSerialization()
    configureSecurity(tokenConfig)
    configureRouting(tokenConfig)
}