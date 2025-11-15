package org.prospex.presentation

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import org.prospex.infrastructure.seeders.QuestionSeeder
import org.prospex.infrastructure.seeders.SupportMeasureSeeder
import org.prospex.presentation.di.Koin

class ProspexApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Koin.init(this)
        
        applicationScope.launch {
            val questionSeeder = GlobalContext.get().get<QuestionSeeder>()
            questionSeeder.seedIfEmpty()
            
            val supportMeasureSeeder = GlobalContext.get().get<SupportMeasureSeeder>()
            supportMeasureSeeder.seedIfEmpty()
        }
    }
}

