package org.prospex.presentation.providers

import org.prospex.infrastructure.utilities.IAuthProvider

class AuthProvider : IAuthProvider {
    override suspend fun getSigningKey(): String {
        return "strong_strong_strong_secret_signing_key" // ideal, read from .env
    }
}