package org.prospex.infrastructure.repositories

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.domain.repositories.IAuthRepository
import org.prospex.domain.repositories.IUserRepository
import org.prospex.domain.value_objects.Credentials
import org.prospex.domain.value_objects.Email
import org.prospex.domain.value_objects.JWT
import org.prospex.domain.value_objects.PasswordHash
import org.prospex.infrastructure.datasources.CredentialsDatasource
import org.prospex.infrastructure.utilities.IAuthProvider
import java.util.*

class AuthRepository(
    private val provider: IAuthProvider,
    private val userRepository: IUserRepository,
    private val unitOfWork: IUnitOfWork
) : IAuthRepository {
    override suspend fun saveCredentials(credentials: Credentials) {
        unitOfWork.execute {
            CredentialsDatasource.insert {
                it[email] = credentials.email.value
                it[passwordHash] = credentials.passwordHash.hash
            }
        }
    }

    override suspend fun getCredentials(email: Email): Credentials? {
        return unitOfWork.execute {
            CredentialsDatasource
                .selectAll()
                .where { CredentialsDatasource.email eq email.value }
                .map {
                    Credentials(
                        email = Email(it[CredentialsDatasource.email].value),
                        passwordHash = PasswordHash(it[CredentialsDatasource.passwordHash])
                    )
                }
                .firstOrNull()
        }
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