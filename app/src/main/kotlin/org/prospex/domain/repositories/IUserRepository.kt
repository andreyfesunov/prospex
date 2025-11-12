package org.prospex.domain.repositories

import org.prospex.domain.models.User
import org.prospex.domain.value_objects.Email
import java.util.UUID

interface IUserRepository {
    suspend fun create(user: User)
    suspend fun getByEmail(email: Email): User?
    suspend fun getById(id: UUID): User?
}