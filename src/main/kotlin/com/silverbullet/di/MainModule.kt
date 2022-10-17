package com.silverbullet.di

import com.silverbullet.user.data.UserDataSource
import com.silverbullet.user.data.UserDataSourceImpl
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

    single<UserDataSource> {
        UserDataSourceImpl(get())
    }

}