package org.prospex.application.usecases

import org.prospex.application.utilities.IAuthContext
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.application.utilities.Result
import org.prospex.application.utilities.UseCase
import org.prospex.domain.models.Idea
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.SurveyResponse
import org.prospex.domain.repositories.IIdeaRepository
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Score
import java.util.*

class CreateIdeaUseCase(
    private val unitOfWork: IUnitOfWork,
    private val authContext: IAuthContext,
    private val ideaRepository: IIdeaRepository,
    private val surveyRepository: ISurveyRepository
) : UseCase<CreateIdeaUseCase.Params, Idea> {
    data class IdeaParams(
        val title: String,
        val description: String,
        val legalType: LegalType,
    )

    data class Params(
        val idea: IdeaParams,
        val surveyResponse: SurveyResponse
    )

    override suspend fun execute(params: Params): Result<Idea> {
        val userId = authContext.getUserId()
        val requiredQuestionIds = surveyRepository
            .getQuestionsByLegalType(params.idea.legalType)
            .map { it.id }
        val answeredQuestionIds = surveyRepository
            .getQuestionsByOptionIds(params.surveyResponse.optionIds)
            .map { it.id }

        if (!requiredQuestionIds.toSet().containsAll(answeredQuestionIds.toSet()) || !answeredQuestionIds.toSet()
                .containsAll(requiredQuestionIds.toSet())
        ) {
            return Result.Error("You haven't answered all the required survey questions.")
        }

        val idea = Idea(
            id = UUID.randomUUID(),
            userId = userId,
            title = params.idea.title,
            description = params.idea.description,
            legalType = params.idea.legalType,
            score = Score(
                surveyRepository
                    .getOptionsByIds(params.surveyResponse.optionIds)
                    .sumOf { it.score.value }
            )
        )

        return unitOfWork.execute {
            ideaRepository.create(idea)
            surveyRepository.create(params.surveyResponse)
            Result.Success(idea)
        }
    }
}