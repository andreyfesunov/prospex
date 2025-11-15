package org.prospex.presentation.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import org.prospex.application.usecases.CreateIdeaUseCase
import org.prospex.application.usecases.DeleteIdeaUseCase
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
import org.prospex.infrastructure.database.ProspexDatabase
import org.prospex.infrastructure.repositories.AuthRepository
import org.prospex.infrastructure.repositories.IdeaRepository
import org.prospex.infrastructure.repositories.SupportMeasureRepository
import org.prospex.infrastructure.repositories.SurveyRepository
import org.prospex.infrastructure.repositories.UserRepository
import org.prospex.infrastructure.utilities.AuthContext
import org.prospex.infrastructure.utilities.IAuthProvider
import org.prospex.presentation.providers.AuthProvider

object AppModule {
    fun init(): Module {
        return module {
            // Database
            single { ProspexDatabase.getDatabase(androidContext()) }
            
            // DAOs
            single { get<ProspexDatabase>().userDao() }
            single { get<ProspexDatabase>().credentialsDao() }
            single { get<ProspexDatabase>().ideaDao() }
            single { get<ProspexDatabase>().questionDao() }
            single { get<ProspexDatabase>().questionOptionDao() }
            single { get<ProspexDatabase>().surveyResponseDao() }
            single { get<ProspexDatabase>().supportMeasureDao() }
            
            // Utils
            single<IUnitOfWork> { org.prospex.infrastructure.utilities.UnitOfWork(get()) }
            single<IAuthProvider> { AuthProvider() }
            single<ISettableAuthContext> { AuthContext(get()) }
            single<IAuthContext> { get<ISettableAuthContext>() }

            // Repositories
            single<IAuthRepository> { AuthRepository(get(), get(), get()) }
            single<IIdeaRepository> { IdeaRepository(get()) }
            single<ISupportMeasureRepository> { SupportMeasureRepository(get()) }
            single<ISurveyRepository> { SurveyRepository(get(), get(), get()) }
            single<IUserRepository> { UserRepository(get()) }

            // UseCases
            single { CreateIdeaUseCase(get(), get(), get(), get()) }
            single { DeleteIdeaUseCase(get(), get(), get()) }
            single { GetIdeasUseCase(get(), get()) }
            single { SignInUseCase(get()) }
            single { SignUpUseCase(get(), get(), get()) }
            single { UpdateIdeaUseCase(get(), get(), get(), get()) }

            // Seeders
            single { org.prospex.infrastructure.seeders.QuestionSeeder(get()) }
            single { org.prospex.infrastructure.seeders.SupportMeasureSeeder(get()) }
        }
    }
}
