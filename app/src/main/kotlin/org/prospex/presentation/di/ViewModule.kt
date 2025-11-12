package org.prospex.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.prospex.presentation.viewmodels.AuthViewModel
import org.prospex.presentation.viewmodels.CreateIdeaViewModel
import org.prospex.presentation.viewmodels.IdeasListViewModel

object ViewModule {
    fun init(): Module {
        return module {
            viewModel { AuthViewModel(get(), get(), get(), get()) }
            viewModel { IdeasListViewModel(get()) }
            viewModel { CreateIdeaViewModel(get(), get()) }
        }
    }
}