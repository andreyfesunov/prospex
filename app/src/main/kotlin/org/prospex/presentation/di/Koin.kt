package org.prospex.presentation.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object Koin {
    fun init(context: Context) {
        startKoin {
            androidContext(context)
            modules(
                AppModule.init(),
                ViewModule.init()
            )
        }
    }
}