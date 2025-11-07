package org.prospex.domain.repositories

import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.SurveyResponse

interface ISurveyRepository {
    suspend fun create(question: Question)
    suspend fun create(questionOption: QuestionOption)
    suspend fun create(surveyResponse: SurveyResponse)
}