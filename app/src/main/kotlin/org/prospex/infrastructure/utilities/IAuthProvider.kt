package org.prospex.infrastructure.utilities

interface IAuthProvider {
    suspend fun getSigningKey(): String
}