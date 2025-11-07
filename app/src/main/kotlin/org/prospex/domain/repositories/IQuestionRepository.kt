package org.prospex.domain.repositories

import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption

interface IQuestionRepository {
    suspend fun create(question: Question)
    suspend fun create(questionOption: QuestionOption)
}