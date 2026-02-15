package org.prospex.domain.repositories

import org.prospex.domain.models.LegalTypeSurveyOption
import org.prospex.domain.models.LegalTypeSurveyQuestion

interface ILegalTypeSurveyRepository {
    suspend fun createQuestion(question: LegalTypeSurveyQuestion)
    suspend fun createOption(option: LegalTypeSurveyOption)
    suspend fun getAllQuestions(): List<LegalTypeSurveyQuestion>
    suspend fun getOptionsByQuestionId(questionId: java.util.UUID): List<LegalTypeSurveyOption>
    suspend fun hasAnyQuestions(): Boolean
}
