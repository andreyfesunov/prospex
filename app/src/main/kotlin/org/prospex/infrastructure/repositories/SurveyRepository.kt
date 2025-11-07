package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.r2dbc.insert
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.SurveyResponse
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.infrastructure.datasources.QuestionOptionsDatasource
import org.prospex.infrastructure.datasources.QuestionsDatasource
import org.prospex.infrastructure.datasources.SurveyResponsesDatasource

class SurveyRepository : ISurveyRepository {
    override suspend fun create(question: Question) {
        QuestionsDatasource.insert {
            it[id] = question.id
            it[text] = question.text
            it[legalType] = question.legalType
        }
    }

    override suspend fun create(questionOption: QuestionOption) {
        QuestionOptionsDatasource.insert {
            it[id] = questionOption.id
            it[questionId] = questionOption.questionId
            it[text] = questionOption.text
            it[score] = questionOption.score.value
        }
    }

    override suspend fun create(surveyResponse: SurveyResponse) {
        SurveyResponsesDatasource.insert {
            it[ideaId] = surveyResponse.ideaId
            it[optionIds] = surveyResponse.optionIds.asList()
        }
    }
}