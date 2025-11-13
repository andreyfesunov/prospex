package org.prospex.infrastructure.repositories

import org.prospex.domain.models.User
import org.prospex.domain.repositories.IUserRepository
import org.prospex.domain.value_objects.Email
import org.prospex.infrastructure.database.dao.UserDao
import org.prospex.infrastructure.database.entities.UserEntity
import java.util.UUID

class UserRepository(
    private val userDao: UserDao
) : IUserRepository {
    override suspend fun create(user: User) {
        userDao.insert(UserEntity.fromDomain(user.id, user.email.value))
    }

    override suspend fun getByEmail(email: Email): User? {
        val entity = userDao.getByEmail(email.value) ?: return null
        return User(
            id = UUID.fromString(entity.id),
            email = Email(entity.email)
        )
    }

    override suspend fun getById(id: UUID): User? {
        val entity = userDao.getById(id.toString()) ?: return null
        return User(
            id = UUID.fromString(entity.id),
            email = Email(entity.email)
        )
    }
}
