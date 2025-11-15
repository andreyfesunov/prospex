package org.prospex.domain.repositories

import org.prospex.domain.models.Idea
import org.prospex.domain.models.PageModel
import org.prospex.domain.value_objects.Positive
import java.util.*

interface IIdeaRepository {
    suspend fun create(idea: Idea)
    suspend fun update(idea: Idea)
    suspend fun delete(id: UUID)
    suspend fun get(id: UUID): Idea?
    suspend fun get(filters: IdeaFilters): PageModel<Idea>
}

data class IdeaFilters(
    val userId: UUID,
    val page: Positive,
    val pageSize: Positive
)