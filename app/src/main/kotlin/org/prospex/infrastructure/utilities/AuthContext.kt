package org.prospex.infrastructure.utilities

import org.prospex.application.utilities.IAuthContext
import org.prospex.domain.value_objects.JWT
import java.util.UUID

interface ISettableAuthContext : IAuthContext {
    fun setJwt(jwt: JWT?)
    fun getJwt(): JWT?
    suspend fun getEmail(): String?
}

class AuthContext(
    private val authProvider: IAuthProvider
) : ISettableAuthContext {
    private var jwt: JWT? = null

    override fun setJwt(jwt: JWT?) {
        this.jwt = jwt
    }

    override fun getJwt(): JWT? {
        return jwt
    }

    override suspend fun getEmail(): String? {
        val currentJwt = jwt ?: return null
        val signingKey = authProvider.getSigningKey()
        return currentJwt.getEmail(signingKey)
    }

    override suspend fun getUserId(): UUID {
        val currentJwt = jwt ?: throw IllegalStateException("User is not authenticated")
        val signingKey = authProvider.getSigningKey()
        return currentJwt.getUserId(signingKey) 
            ?: throw IllegalStateException("Unable to extract userId from JWT")
    }
}

