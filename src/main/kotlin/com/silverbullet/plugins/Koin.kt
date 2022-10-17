package com.silverbullet.plugins

import com.silverbullet.di.mainModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {

    install(Koin) {
        modules(mainModule)
    }

}