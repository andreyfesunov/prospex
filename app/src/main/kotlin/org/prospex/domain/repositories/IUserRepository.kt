package org.prospex.domain.repositories

import org.prospex.domain.models.User
import org.prospex.domain.value_objects.Email

interface IUserRepository {
    suspend fun create(user: User)
    suspend fun getByEmail(email: Email): User?
}