package org.prospex.presentation.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.prospex.application.usecases.CreateIdeaUseCase
import org.prospex.application.usecases.GetIdeasUseCase
import org.prospex.application.usecases.SignInUseCase
import org.prospex.application.usecases.SignUpUseCase
import org.prospex.application.usecases.UpdateIdeaUseCase
import org.prospex.application.utilities.IAuthContext
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.infrastructure.utilities.ISettableAuthContext
import org.prospex.domain.repositories.IAuthRepository
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISupportMeasureRepository
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.repositories.IUserRepository
import org.prospex.infrastructure.repositories.AuthRepository
import org.prospex.infrastructure.repositories.IdeaRepository
import org.prospex.infrastructure.repositories.SupportMeasureRepository
import org.prospex.infrastructure.repositories.SurveyRepository
import org.prospex.infrastructure.repositories.UserRepository
import org.prospex.infrastructure.utilities.AuthContext
import org.prospex.infrastructure.utilities.IAuthProvider
import org.prospex.infrastructure.utilities.UnitOfWork
import org.prospex.presentation.providers.AuthProvider

object AppModule {
    fun init(): Module {
        return module {
            // Utils
            single<IUnitOfWork> { UnitOfWork() }
            single<IAuthProvider> { AuthProvider() }
            single<ISettableAuthContext> { AuthContext(get()) }
            single<IAuthContext> { get<ISettableAuthContext>() }

            // Repositories
            single<IAuthRepository> { AuthRepository(get(), get()) }
            single<IIdeaRepository> { IdeaRepository() }
            single<ISupportMeasureRepository> { SupportMeasureRepository() }
            single<ISurveyRepository> { SurveyRepository() }
            single<IUserRepository> { UserRepository() }

            // UseCases
            single { CreateIdeaUseCase(get(), get(), get(), get()) }
            single { GetIdeasUseCase(get(), get()) }
            single { SignInUseCase(get()) }
            single { SignUpUseCase(get(), get(), get()) }
            single { UpdateIdeaUseCase(get(), get(), get(), get()) }
        }
    }
}