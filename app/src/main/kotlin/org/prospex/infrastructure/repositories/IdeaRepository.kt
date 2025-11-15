package org.prospex.infrastructure.repositories

import org.prospex.domain.models.Idea
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.PageModel
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.IdeaFilters
import org.prospex.domain.value_objects.Score
import org.prospex.infrastructure.database.dao.IdeaDao
import org.prospex.infrastructure.database.entities.IdeaEntity
import java.util.*

class IdeaRepository(
    private val ideaDao: IdeaDao
) : IIdeaRepository {
    override suspend fun create(idea: Idea) {
        ideaDao.insert(
            IdeaEntity.fromDomain(
                idea.id,
                idea.userId,
                idea.title,
                idea.description,
                idea.legalType,
                idea.score.value
            )
        )
    }

    override suspend fun update(idea: Idea) {
        ideaDao.update(
            IdeaEntity.fromDomain(
                idea.id,
                idea.userId,
                idea.title,
                idea.description,
                idea.legalType,
                idea.score.value
            )
        )
    }

    override suspend fun delete(id: UUID) {
        val entity = ideaDao.getById(id.toString()) ?: return
        ideaDao.delete(entity)
    }

    override suspend fun get(id: UUID): Idea? {
        val entity = ideaDao.getById(id.toString()) ?: return null
        return Idea(
            id = UUID.fromString(entity.id),
            userId = UUID.fromString(entity.userId),
            title = entity.title,
            description = entity.description,
            legalType = LegalType.valueOf(entity.legalType),
            score = Score(entity.score.toUInt())
        )
    }

    override suspend fun get(filters: IdeaFilters): PageModel<Idea> {
        val offset = ((filters.page.value - 1u) * filters.pageSize.value).toInt()
        val limit = filters.pageSize.value.toInt()
        
        val entities = ideaDao.getByUserId(filters.userId.toString(), limit, offset)
        val totalItems = ideaDao.countByUserId(filters.userId.toString()).toUInt()
        
        val items = entities.map { entity ->
            Idea(
                id = UUID.fromString(entity.id),
                userId = UUID.fromString(entity.userId),
                title = entity.title,
                description = entity.description,
                legalType = LegalType.valueOf(entity.legalType),
                score = Score(entity.score.toUInt())
            )
        }.toTypedArray()
        
        return PageModel(
            items = items,
            page = filters.page,
            pageSize = filters.pageSize,
            totalItems = totalItems
        )
    }
}
