package org.prospex.infrastructure.utilities

import org.prospex.application.utilities.IAuthContext
import org.prospex.domain.value_objects.JWT
import java.util.UUID

interface ISettableAuthContext : IAuthContext {
    fun setJwt(jwt: JWT?)
}

class AuthContext(
    private val authProvider: IAuthProvider
) : ISettableAuthContext {
    private var jwt: JWT? = null

    override fun setJwt(jwt: JWT?) {
        this.jwt = jwt
    }

    override suspend fun getUserId(): UUID {
        val currentJwt = jwt ?: throw IllegalStateException("User is not authenticated")
        val signingKey = authProvider.getSigningKey()
        return currentJwt.getUserId(signingKey) 
            ?: throw IllegalStateException("Unable to extract userId from JWT")
    }
}

