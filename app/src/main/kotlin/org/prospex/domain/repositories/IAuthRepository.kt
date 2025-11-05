package org.prospex.domain.repositories

import org.prospex.domain.value_objects.Credentials
import org.prospex.domain.value_objects.JWT

interface IAuthRepository {
    suspend fun saveCredentials(credentials: Credentials)
    suspend fun authenticate(credentials: Credentials): JWT?
}