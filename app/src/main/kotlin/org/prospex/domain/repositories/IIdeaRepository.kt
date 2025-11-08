package org.prospex.domain.repositories

import org.prospex.domain.models.Idea
import java.util.*

interface IIdeaRepository {
    suspend fun create(idea: Idea)
    suspend fun update(idea: Idea)
    suspend fun get(id: UUID): Idea?
}