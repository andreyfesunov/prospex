package org.prospex.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prospex.application.usecases.SignInUseCase
import org.prospex.application.usecases.SignUpUseCase
import org.prospex.application.utilities.Result
import org.prospex.domain.value_objects.JWT
import org.prospex.infrastructure.utilities.IAuthProvider
import org.prospex.infrastructure.utilities.ISettableAuthContext

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val authProvider: IAuthProvider,
    private val authContext: ISettableAuthContext,
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _jwt = MutableStateFlow<JWT?>(null)
    val jwt: StateFlow<JWT?> = _jwt.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val jwtResult = signInUseCase.execute(SignInUseCase.Params(email, password))

            if (jwtResult is Result.Success) {
                _jwt.value = jwtResult.data
                authContext.setJwt(jwtResult.data)
                _authState.value = AuthState.Authenticated(jwtResult.data)
                _errorMessage.value = null
            }
            else if (jwtResult is Result.Error) {
                _authState.value = AuthState.Unauthenticated
                _errorMessage.value = "Ошибка входа: ${jwtResult.message}"
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            if (password != confirmPassword) {
                _errorMessage.value = "Пароли не совпадают"
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            val regResult = signUpUseCase.execute(SignUpUseCase.Params(email, password))

            if (regResult is Result.Error) {
                _authState.value = AuthState.Unauthenticated
                _errorMessage.value = "Ошибка регистрации: ${regResult.message}"
                return@launch
            }

            signIn(email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _jwt.value = null
            authContext.setJwt(null)
            _authState.value = AuthState.Unauthenticated
            _errorMessage.value = null
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    suspend fun getEmailFromJwt(): String? {
        val jwt = _jwt.value ?: return null
        val signingKey = authProvider.getSigningKey()
        return jwt.getEmail(signingKey)
    }
}

sealed class AuthState {
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Authenticated(val jwt: JWT) : AuthState()
}