package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.jdbc.insert
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository
import org.prospex.infrastructure.datasources.SupportMeasuresDatasource

class SupportMeasureRepository : ISupportMeasureRepository {
    override suspend fun create(measure: SupportMeasure) {
        SupportMeasuresDatasource.insert {
            it[id] = measure.id
            it[title] = measure.title
            it[minScore] = measure.minScore.value
        }
    }
}