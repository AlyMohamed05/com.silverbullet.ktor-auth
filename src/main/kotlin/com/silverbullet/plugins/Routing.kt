package com.silverbullet.plugins

import com.silverbullet.auth.routes.authenticate
import com.silverbullet.auth.routes.getSecretInfo
import com.silverbullet.auth.routes.signIn
import com.silverbullet.auth.routes.signUp
import com.silverbullet.security.token.TokenConfig
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(tokenConfig: TokenConfig) {

    routing {
        signUp()
        signIn(tokenConfig)
        authenticate()
        getSecretInfo()
    }
}
