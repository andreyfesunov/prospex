package org.prospex.infrastructure.repositories

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import org.prospex.domain.models.Idea
import org.prospex.domain.models.PageModel
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.IdeaFilters
import org.prospex.domain.value_objects.Score
import org.prospex.infrastructure.datasources.IdeasDatasource
import java.util.*

class IdeaRepository : IIdeaRepository {
    override suspend fun create(idea: Idea) {
        IdeasDatasource.insert {
            it[id] = idea.id
            it[userId] = idea.userId
            it[title] = idea.title
            it[description] = idea.description
            it[legalType] = idea.legalType
            it[score] = idea.score.value
        }
    }

    override suspend fun update(idea: Idea) {
        IdeasDatasource.update({ IdeasDatasource.id eq idea.id }) {
            it[userId] = idea.userId
            it[title] = idea.title
            it[description] = idea.description
            it[legalType] = idea.legalType
            it[score] = idea.score.value
        }
    }

    override suspend fun get(id: UUID): Idea? {
        return IdeasDatasource
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

    override suspend fun get(filters: IdeaFilters): PageModel<Idea> {
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

        return PageModel(
            items = paginatedItems,
            page = filters.page,
            pageSize = filters.pageSize,
            totalItems = totalItems
        )
    }
}