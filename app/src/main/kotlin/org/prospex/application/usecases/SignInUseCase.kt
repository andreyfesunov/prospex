package org.prospex.application.usecases

import org.prospex.domain.value_objects.JWT
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.repositories.IAuthRepository
import org.prospex.domain.value_objects.Credentials
import org.prospex.domain.value_objects.Email
import org.prospex.domain.value_objects.PasswordHash

class SignInUseCase(
    private val authRepository: IAuthRepository
) : UseCase<SignInUseCase.Params, JWT> {
    data class Params(val email: String, val password: String)

    override suspend fun execute(params: Params): Result<JWT> {
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

        authRepository.getCredentials(email) ?: return Result.Error("Not found user with provided email.")

        val jwt = authRepository.authenticate(Credentials(email, passwordHash)) ?: return Result.Error("Error while authenticating user.")

        return Result.Success(jwt)
    }
}