package com.silverbullet.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {

    single {
        KMongo
            .createClient()
            .coroutine
            .getDatabase("ktor-auth")
    }

}