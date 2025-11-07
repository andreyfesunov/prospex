package org.prospex.domain.repositories

import org.prospex.domain.models.Idea

interface IIdeaRepository {
    suspend fun create(idea: Idea)
}