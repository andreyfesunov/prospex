package org.prospex.infrastructure.repositories

import org.prospex.domain.models.LegalTypeSurveyOption
import org.prospex.domain.models.LegalTypeSurveyQuestion
import org.prospex.domain.repositories.ILegalTypeSurveyRepository
import org.prospex.infrastructure.database.dao.LegalTypeSurveyOptionDao
import org.prospex.infrastructure.database.dao.LegalTypeSurveyQuestionDao
import org.prospex.infrastructure.database.entities.LegalTypeSurveyOptionEntity
import org.prospex.infrastructure.database.entities.LegalTypeSurveyQuestionEntity
import java.util.UUID

class LegalTypeSurveyRepository(
    private val questionDao: LegalTypeSurveyQuestionDao,
    private val optionDao: LegalTypeSurveyOptionDao
) : ILegalTypeSurveyRepository {
    override suspend fun createQuestion(question: LegalTypeSurveyQuestion) {
        questionDao.insert(LegalTypeSurveyQuestionEntity.fromDomain(question))
    }

    override suspend fun createOption(option: LegalTypeSurveyOption) {
        optionDao.insert(LegalTypeSurveyOptionEntity.fromDomain(option))
    }

    override suspend fun getAllQuestions(): List<LegalTypeSurveyQuestion> {
        return questionDao.getAll().map { LegalTypeSurveyQuestionEntity.toDomain(it) }
    }

    override suspend fun getOptionsByQuestionId(questionId: UUID): List<LegalTypeSurveyOption> {
        return optionDao.getByQuestionId(questionId.toString()).map { LegalTypeSurveyOptionEntity.toDomain(it) }
    }

    override suspend fun hasAnyQuestions(): Boolean {
        return questionDao.getAll().isNotEmpty()
    }
}
