package org.prospex.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.prospex.presentation.viewmodels.AuthViewModel
import org.prospex.presentation.viewmodels.CreateIdeaViewModel
import org.prospex.presentation.viewmodels.IdeaDetailsViewModel
import org.prospex.presentation.viewmodels.IdeasListViewModel
import org.prospex.presentation.viewmodels.SupportMeasuresViewModel

object ViewModule {
    fun init(): Module {
        return module {
            viewModel { AuthViewModel(get(), get(), get(), get()) }
            viewModel { IdeasListViewModel(get(), get()) }
            viewModel { CreateIdeaViewModel(get(), get()) }
            viewModel { IdeaDetailsViewModel(get(), get(), get()) }
            viewModel { SupportMeasuresViewModel(get()) }
        }
    }
}