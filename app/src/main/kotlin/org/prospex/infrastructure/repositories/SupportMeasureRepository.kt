package org.prospex.infrastructure.repositories

import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository
import org.prospex.infrastructure.database.dao.SupportMeasureDao
import org.prospex.infrastructure.database.entities.SupportMeasureEntity

class SupportMeasureRepository(
    private val supportMeasureDao: SupportMeasureDao
) : ISupportMeasureRepository {
    override suspend fun create(measure: SupportMeasure) {
        supportMeasureDao.insert(
            SupportMeasureEntity.fromDomain(
                measure.id,
                measure.title,
                measure.minScore.value
            )
        )
    }
}
