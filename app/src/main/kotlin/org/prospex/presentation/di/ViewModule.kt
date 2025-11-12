package org.prospex.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.prospex.presentation.viewmodels.AuthViewModel

object ViewModule {
    fun init(): Module {
        return module {
            viewModel { AuthViewModel(get(), get()) }
        }
    }
}