package org.prospex.infrastructure.repositories

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.update
import org.prospex.domain.models.Idea
import org.prospex.domain.repositories.IIdeaRepository
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
}