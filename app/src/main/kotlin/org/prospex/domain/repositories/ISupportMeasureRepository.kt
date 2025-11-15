package org.prospex.domain.repositories

import org.prospex.domain.models.SupportMeasure

interface ISupportMeasureRepository {
    suspend fun create(measure: SupportMeasure)
    suspend fun getAll(): Array<SupportMeasure>
    suspend fun hasAny(): Boolean
}