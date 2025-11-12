package org.prospex.application.usecases

import org.prospex.application.utilities.IUnitOfWork
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.models.User
import org.prospex.domain.repositories.IAuthRepository
import org.prospex.domain.repositories.IUserRepository
import org.prospex.domain.value_objects.Credentials
import org.prospex.domain.value_objects.Email
import org.prospex.domain.value_objects.PasswordHash
import java.util.UUID

class SignUpUseCase(
    private val unitOfWork: IUnitOfWork,
    private val userRepository: IUserRepository,
    private val authRepository: IAuthRepository
) : UseCase<SignUpUseCase.Params, User> {
    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun execute(params: Params): Result<User> {
        val email = try {
            Email(params.email)
        } catch (e: IllegalArgumentException) {
            return Result.Error("Error while creating email: ${e.message}")
        }

        val passwordHash = try {
            PasswordHash.fromPlainText(params.password)
        } catch (e: IllegalArgumentException) {
            return Result.Error("Error while creating password hash: ${e.message}")
        }

        return unitOfWork.execute {
            val user = User(id = UUID.randomUUID(), email = email)
            userRepository.create(user = user)
            authRepository.saveCredentials(Credentials(email = email, passwordHash = passwordHash))
            Result.Success(data = user)
        }
    }
}