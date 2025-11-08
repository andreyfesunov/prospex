package org.prospex.application.usecases

import org.prospex.application.utilities.IAuthContext
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.models.Idea
import org.prospex.domain.models.SurveyResponse
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Score
import java.util.*

class UpdateIdeaUseCase(
    private val unitOfWork: IUnitOfWork,
    private val authContext: IAuthContext,
    private val ideaRepository: IIdeaRepository,
    private val surveyRepository: ISurveyRepository
) : UseCase<UpdateIdeaUseCase.Params, Idea> {
    data class IdeaParams(
        val id: UUID,
        val title: String,
        val description: String
    )

    data class Params(
        val idea: IdeaParams,
        val surveyResponse: SurveyResponse
    )

    override suspend fun execute(params: Params): Result<Idea> {
        val idea = ideaRepository.get(params.idea.id) ?: return Result.Error("Idea not found.")
        val userId = authContext.getUserId()

        if (idea.userId != userId) return Result.Error("You can't edit this idea.")

        val requiredQuestionIds = surveyRepository
            .getQuestionsByLegalType(idea.legalType)
            .map { it.id }
        val answeredQuestionIds = surveyRepository
            .getQuestionsByOptionIds(params.surveyResponse.optionIds)
            .map { it.id }

        if (!requiredQuestionIds.toSet().containsAll(answeredQuestionIds.toSet()) || !answeredQuestionIds.toSet()
                .containsAll(requiredQuestionIds.toSet())
        ) {
            return Result.Error("You haven't answered all the required survey questions.")
        }

        val newIdea = Idea(
            id = idea.id,
            userId = userId,
            title = params.idea.title,
            description = params.idea.description,
            legalType = idea.legalType,
            score = Score(
                surveyRepository
                    .getOptionsByIds(params.surveyResponse.optionIds)
                    .sumOf { it.score.value }
            )
        )

        return unitOfWork.execute {
            ideaRepository.update(newIdea)
            surveyRepository.update(params.surveyResponse)
            Result.Success(newIdea)
        }
    }
}