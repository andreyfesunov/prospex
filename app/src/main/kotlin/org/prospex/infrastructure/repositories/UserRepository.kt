package org.prospex.infrastructure.repositories

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.prospex.domain.models.User
import org.prospex.domain.repositories.IUserRepository
import org.prospex.domain.value_objects.Email
import org.prospex.infrastructure.datasources.UsersDatasource

class UserRepository : IUserRepository {
    override suspend fun create(user: User) {
        UsersDatasource.insert {
            it[id] = user.id
            it[email] = user.email.value
        }
    }

    override suspend fun getByEmail(email: Email): User? {
        return UsersDatasource
            .selectAll()
            .where { UsersDatasource.email eq email.value }
            .map { User(id = it[UsersDatasource.id].value, email = Email(it[UsersDatasource.email])) }
            .firstOrNull()
    }
}