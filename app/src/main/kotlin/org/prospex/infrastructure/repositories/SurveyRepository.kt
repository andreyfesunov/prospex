package org.prospex.infrastructure.repositories

import org.prospex.domain.models.LegalType
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.QuestionType
import org.prospex.domain.models.SurveyResponse
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Score
import org.prospex.infrastructure.database.dao.QuestionDao
import org.prospex.infrastructure.database.dao.QuestionOptionDao
import org.prospex.infrastructure.database.dao.SurveyResponseDao
import org.prospex.infrastructure.database.entities.QuestionEntity
import org.prospex.infrastructure.database.entities.QuestionOptionEntity
import org.prospex.infrastructure.database.entities.SurveyResponseEntity
import java.util.*

class SurveyRepository(
    private val questionDao: QuestionDao,
    private val questionOptionDao: QuestionOptionDao,
    private val surveyResponseDao: SurveyResponseDao
) : ISurveyRepository {
    override suspend fun create(question: Question) {
        questionDao.insert(
            QuestionEntity.fromDomain(
                question.id,
                question.text,
                question.legalType.name,
                question.type.name
            )
        )
    }

    override suspend fun create(questionOption: QuestionOption) {
        questionOptionDao.insert(
            QuestionOptionEntity.fromDomain(
                questionOption.id,
                questionOption.questionId,
                questionOption.text,
                questionOption.score.value
            )
        )
    }

    override suspend fun create(surveyResponse: SurveyResponse) {
        surveyResponseDao.insert(
            SurveyResponseEntity.fromDomain(
                surveyResponse.ideaId,
                surveyResponse.optionIds
            )
        )
    }

    override suspend fun update(surveyResponse: SurveyResponse) {
        surveyResponseDao.update(
            SurveyResponseEntity.fromDomain(
                surveyResponse.ideaId,
                surveyResponse.optionIds
            )
        )
    }

    override suspend fun getSurveyResponse(ideaId: UUID): SurveyResponse? {
        val entity = surveyResponseDao.getByIdeaId(ideaId.toString()) ?: return null
        return SurveyResponse(
            ideaId = UUID.fromString(entity.ideaId),
            optionIds = SurveyResponseEntity.toDomainOptionIds(entity.optionIds)
        )
    }

    override suspend fun getQuestionsByLegalType(legalType: LegalType): Array<Question> {
        val entities = questionDao.getByLegalType(legalType.name)
        return entities.map { entity ->
            Question(
                id = UUID.fromString(entity.id),
                text = entity.text,
                legalType = LegalType.valueOf(entity.legalType),
                type = QuestionType.valueOf(entity.type)
            )
        }.toTypedArray()
    }

    override suspend fun getQuestionsByOptionIds(optionIds: Array<UUID>): Array<Question> {
        val optionIdStrings = optionIds.map { it.toString() }
        val entities = questionDao.getByOptionIds(optionIdStrings)
        return entities.map { entity ->
            Question(
                id = UUID.fromString(entity.id),
                text = entity.text,
                legalType = LegalType.valueOf(entity.legalType),
                type = QuestionType.valueOf(entity.type)
            )
        }.toTypedArray()
    }

    override suspend fun getOptionsByIds(ids: Array<UUID>): Array<QuestionOption> {
        val idStrings = ids.map { it.toString() }
        val entities = questionOptionDao.getByIds(idStrings)
        return entities.map { entity ->
            QuestionOption(
                id = UUID.fromString(entity.id),
                questionId = UUID.fromString(entity.questionId),
                text = entity.text,
                score = Score(entity.score.toUInt())
            )
        }.toTypedArray()
    }

    override suspend fun getOptionsByQuestionId(questionId: UUID): Array<QuestionOption> {
        val entities = questionOptionDao.getByQuestionId(questionId.toString())
        return entities.map { entity ->
            QuestionOption(
                id = UUID.fromString(entity.id),
                questionId = UUID.fromString(entity.questionId),
                text = entity.text,
                score = Score(entity.score.toUInt())
            )
        }.toTypedArray()
    }

    override suspend fun hasAnyOptions(): Boolean {
        return questionOptionDao.count() > 0
    }
}
