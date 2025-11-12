package org.prospex.domain.repositories

import org.prospex.domain.models.LegalType
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.SurveyResponse
import java.util.*

interface ISurveyRepository {
    suspend fun create(question: Question)
    suspend fun create(questionOption: QuestionOption)
    suspend fun create(surveyResponse: SurveyResponse)
    suspend fun update(surveyResponse: SurveyResponse)
    suspend fun getQuestionsByLegalType(legalType: LegalType): Array<Question>
    suspend fun getQuestionsByOptionIds(optionIds: Array<UUID>): Array<Question>
    suspend fun getOptionsByIds(ids: Array<UUID>): Array<QuestionOption>
    suspend fun getOptionsByQuestionId(questionId: UUID): Array<QuestionOption>
}