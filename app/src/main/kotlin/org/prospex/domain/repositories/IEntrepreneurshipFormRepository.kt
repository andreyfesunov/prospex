package org.prospex.domain.repositories

import org.prospex.domain.models.EntrepreneurshipForm

interface IEntrepreneurshipFormRepository {
    suspend fun create(form: EntrepreneurshipForm)
    suspend fun getAll(): Array<EntrepreneurshipForm>
    suspend fun hasAny(): Boolean
}
