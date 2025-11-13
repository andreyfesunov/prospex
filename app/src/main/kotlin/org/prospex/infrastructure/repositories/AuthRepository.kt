package org.prospex.infrastructure.repositories

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.prospex.domain.repositories.IAuthRepository
import org.prospex.domain.repositories.IUserRepository
import org.prospex.domain.value_objects.Credentials
import org.prospex.domain.value_objects.Email
import org.prospex.domain.value_objects.JWT
import org.prospex.domain.value_objects.PasswordHash
import org.prospex.infrastructure.database.dao.CredentialsDao
import org.prospex.infrastructure.database.entities.CredentialsEntity
import org.prospex.infrastructure.utilities.IAuthProvider
import java.util.*

class AuthRepository(
    private val provider: IAuthProvider,
    private val userRepository: IUserRepository,
    private val credentialsDao: CredentialsDao
) : IAuthRepository {
    override suspend fun saveCredentials(credentials: Credentials) {
        credentialsDao.insert(
            CredentialsEntity(
                email = credentials.email.value,
                passwordHash = credentials.passwordHash.hash
            )
        )
    }

    override suspend fun getCredentials(email: Email): Credentials? {
        val entity = credentialsDao.getByEmail(email.value) ?: return null
        return Credentials(
            email = Email(entity.email),
            passwordHash = PasswordHash(entity.passwordHash)
        )
    }

    override suspend fun authenticate(credentials: Credentials): JWT? {
        val data = getCredentials(credentials.email) ?: return null

        if (data.passwordHash != credentials.passwordHash) return null

        val user = userRepository.getByEmail(credentials.email) ?: return null

        val key = Keys.hmacShaKeyFor(provider.getSigningKey().toByteArray())

        val now = System.currentTimeMillis()
        val payload = Jwts.builder()
            .subject(credentials.email.value)
            .claim("email", credentials.email.value)
            .claim("userId", user.id.toString())
            .issuedAt(Date(now))
            .expiration(Date(now + 1000 * 60 * 60))
            .signWith(key)
            .compact()

        return JWT(payload)
    }
}
