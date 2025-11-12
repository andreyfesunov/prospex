package org.prospex.presentation.di

import org.koin.core.context.startKoin

object Koin {
    fun init() {
        startKoin {
            modules(AppModule.init())
        }
    }
}