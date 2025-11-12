package org.prospex.infrastructure.repositories

import org.jetbrains.exposed.v1.core.JoinType
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.SurveyResponse
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Score
import org.prospex.infrastructure.datasources.QuestionOptionsDatasource
import org.prospex.infrastructure.datasources.QuestionsDatasource
import org.prospex.infrastructure.datasources.SurveyResponsesDatasource
import java.util.*

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

    override suspend fun update(surveyResponse: SurveyResponse) {
        SurveyResponsesDatasource.update({ SurveyResponsesDatasource.ideaId eq surveyResponse.ideaId }) {
            it[optionIds] = surveyResponse.optionIds.asList()
        }
    }

    override suspend fun getQuestionsByLegalType(legalType: LegalType): Array<Question> {
        return QuestionsDatasource
            .selectAll()
            .where({ QuestionsDatasource.legalType eq legalType })
            .map {
                Question(
                    id = it[QuestionsDatasource.id].value,
                    text = it[QuestionsDatasource.text],
                    legalType = it[QuestionsDatasource.legalType],
                    type = it[QuestionsDatasource.type]
                )
            }
            .toList()
            .toTypedArray()
    }

    override suspend fun getQuestionsByOptionIds(optionIds: Array<UUID>): Array<Question> {
        return QuestionsDatasource
            .join(
                QuestionOptionsDatasource,
                JoinType.INNER,
                QuestionsDatasource.id,
                QuestionOptionsDatasource.questionId
            )
            .selectAll()
            .where({ QuestionOptionsDatasource.id inList optionIds.toList() })
            .map {
                Question(
                    id = it[QuestionsDatasource.id].value,
                    text = it[QuestionsDatasource.text],
                    legalType = it[QuestionsDatasource.legalType],
                    type = it[QuestionsDatasource.type]
                )
            }
            .toList()
            .toTypedArray()
    }

    override suspend fun getOptionsByIds(ids: Array<UUID>): Array<QuestionOption> {
        return QuestionOptionsDatasource
            .selectAll()
            .where({ QuestionOptionsDatasource.id inList ids.asList() })
            .map {
                QuestionOption(
                    id = it[QuestionOptionsDatasource.id].value,
                    questionId = it[QuestionOptionsDatasource.questionId],
                    text = it[QuestionOptionsDatasource.text],
                    score = Score(it[QuestionOptionsDatasource.score])
                )
            }
            .toList()
            .toTypedArray()
    }
}