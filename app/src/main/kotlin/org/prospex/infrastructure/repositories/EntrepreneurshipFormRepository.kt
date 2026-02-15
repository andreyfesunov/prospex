package org.prospex.infrastructure.repositories

import org.prospex.domain.models.EntrepreneurshipForm
import org.prospex.domain.repositories.IEntrepreneurshipFormRepository
import org.prospex.infrastructure.database.dao.EntrepreneurshipFormDao
import org.prospex.infrastructure.database.entities.EntrepreneurshipFormEntity

class EntrepreneurshipFormRepository(
    private val dao: EntrepreneurshipFormDao
) : IEntrepreneurshipFormRepository {
    override suspend fun create(form: EntrepreneurshipForm) {
        dao.insert(EntrepreneurshipFormEntity.fromDomain(form))
    }

    override suspend fun getAll(): Array<EntrepreneurshipForm> {
        return dao.getAll().map { EntrepreneurshipFormEntity.toDomain(it) }.toTypedArray()
    }

    override suspend fun hasAny(): Boolean {
        return dao.count() > 0
    }
}
