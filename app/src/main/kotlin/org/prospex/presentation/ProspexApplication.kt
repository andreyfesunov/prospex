package org.prospex.presentation

import android.app.Application
import org.prospex.presentation.di.Koin

class ProspexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Koin.init()
    }
}

