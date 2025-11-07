package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.r2dbc.insert
import org.prospex.domain.models.Idea
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.infrastructure.datasources.IdeasDatasource

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
}