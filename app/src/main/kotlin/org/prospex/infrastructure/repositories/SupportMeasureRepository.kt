package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.jdbc.insert
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository
import org.prospex.infrastructure.datasources.SupportMeasuresDatasource

class SupportMeasureRepository(
    private val unitOfWork: IUnitOfWork
) : ISupportMeasureRepository {
    override suspend fun create(measure: SupportMeasure) {
        unitOfWork.execute {
            SupportMeasuresDatasource.insert {
                it[id] = measure.id
                it[title] = measure.title
                it[minScore] = measure.minScore.value
            }
        }
    }
}