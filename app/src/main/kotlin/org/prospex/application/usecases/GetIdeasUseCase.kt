package org.prospex.application.usecases

import org.prospex.application.utilities.IAuthContext
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.models.Idea
import org.prospex.domain.models.PageModel
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.IdeaFilters
import org.prospex.domain.value_objects.Positive

class GetIdeasUseCase(
    private val authContext: IAuthContext,
    private val ideaRepository: IIdeaRepository
) : UseCase<GetIdeasUseCase.Params, PageModel<Idea>> {
    data class Params(
        val page: Positive,
        val pageSize: Positive
    )

    override suspend fun execute(params: Params): Result<PageModel<Idea>> {
        val pageModel = ideaRepository.get(
            IdeaFilters(
                userId = authContext.getUserId(),
                page = params.page,
                pageSize = params.pageSize
            )
        )

        return Result.Success(pageModel)
    }
}