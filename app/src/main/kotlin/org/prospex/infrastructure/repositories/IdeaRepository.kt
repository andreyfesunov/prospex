package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.domain.models.Idea
import org.prospex.domain.models.PageModel
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.IdeaFilters
import org.prospex.domain.value_objects.Score
import org.prospex.infrastructure.datasources.IdeasDatasource
import java.util.*

class IdeaRepository(
    private val unitOfWork: IUnitOfWork
) : IIdeaRepository {
    override suspend fun create(idea: Idea) {
        unitOfWork.execute {
            IdeasDatasource.insert {
                it[id] = idea.id
                it[userId] = idea.userId
                it[title] = idea.title
                it[description] = idea.description
                it[legalType] = idea.legalType
                it[score] = idea.score.value
            }
        }
    }

    override suspend fun update(idea: Idea) {
        unitOfWork.execute {
            IdeasDatasource.update({ IdeasDatasource.id eq idea.id }) {
                it[userId] = idea.userId
                it[title] = idea.title
                it[description] = idea.description
                it[legalType] = idea.legalType
                it[score] = idea.score.value
            }
        }
    }

    override suspend fun get(id: UUID): Idea? {
        return unitOfWork.execute {
            IdeasDatasource
                .selectAll()
                .where({ IdeasDatasource.id eq id })
                .map {
                    Idea(
                        id = it[IdeasDatasource.id].value,
                        userId = it[IdeasDatasource.userId],
                        title = it[IdeasDatasource.title],
                        description = it[IdeasDatasource.description],
                        legalType = it[IdeasDatasource.legalType],
                        score = Score(it[IdeasDatasource.score])
                    )
                }
                .firstOrNull()
        }
    }

    override suspend fun get(filters: IdeaFilters): PageModel<Idea> {
        return unitOfWork.execute {
            val builder = { IdeasDatasource.userId eq filters.userId }

            val query = IdeasDatasource
                .selectAll()
                .where(builder)

            val paginatedItems = query
                .offset(((filters.page.value - 1u) * filters.pageSize.value).toLong())
                .take(filters.pageSize.value.toInt())
                .map {
                    Idea(
                        id = it[IdeasDatasource.id].value,
                        userId = it[IdeasDatasource.userId],
                        title = it[IdeasDatasource.title],
                        description = it[IdeasDatasource.description],
                        legalType = it[IdeasDatasource.legalType],
                        score = Score(it[IdeasDatasource.score])
                    )
                }
                .toList()
                .toTypedArray()

            val totalItems = query
                .count()
                .toUInt()

            PageModel(
                items = paginatedItems,
                page = filters.page,
                pageSize = filters.pageSize,
                totalItems = totalItems
            )
        }
    }
}