package org.prospex.application.usecases

import org.prospex.application.utilities.IAuthContext
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.repositories.IIdeaRepository
import java.util.*

class DeleteIdeaUseCase(
    private val unitOfWork: IUnitOfWork,
    private val authContext: IAuthContext,
    private val ideaRepository: IIdeaRepository
) : UseCase<UUID, Unit> {
    override suspend fun execute(params: UUID): Result<Unit> {
        val idea = ideaRepository.get(params) ?: return Result.Error("Idea not found.")
        val userId = authContext.getUserId()

        if (idea.userId != userId) return Result.Error("You can't delete this idea.")

        return unitOfWork.execute {
            ideaRepository.delete(params)
            Result.Success(Unit)
        }
    }
}

