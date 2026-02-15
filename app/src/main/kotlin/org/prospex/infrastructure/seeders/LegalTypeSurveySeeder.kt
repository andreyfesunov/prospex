package org.prospex.infrastructure.seeders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.LegalTypeSurveyOption
import org.prospex.domain.models.LegalTypeSurveyQuestion
import org.prospex.domain.repositories.ILegalTypeSurveyRepository
import java.util.UUID

class LegalTypeSurveySeeder(
    private val repository: ILegalTypeSurveyRepository
) {
    suspend fun seedIfEmpty() {
        withContext(Dispatchers.IO) {
            if (!repository.hasAnyQuestions()) {
                seedSurvey()
            }
        }
    }

    private suspend fun seedSurvey() {
        val q1 = LegalTypeSurveyQuestion(UUID.randomUUID(), "Какой у вас планируемый масштаб деятельности?", 1)
        repository.createQuestion(q1)
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q1.id, "Работа один/одна, без наёмных сотрудников", LegalType.SelfEmployed, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q1.id, "Небольшая деятельность с возможностью нанять 1–5 человек", LegalType.IndividualEntrepreneur, 2))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q1.id, "Фермерское или подсобное хозяйство", LegalType.PersonalSubsidiaryFarm, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q1.id, "Компания с несколькими учредителями и сотрудниками", LegalType.LLC, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q1.id, "Социальный или просветительский проект", LegalType.SocialEntrepreneur, 3))

        val q2 = LegalTypeSurveyQuestion(UUID.randomUUID(), "Какова основная цель вашей деятельности?", 2)
        repository.createQuestion(q2)
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q2.id, "Дополнительный доход без оформления работников", LegalType.SelfEmployed, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q2.id, "Стабильный бизнес с возможностью кредитов и контрактов", LegalType.IndividualEntrepreneur, 2))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q2.id, "Производство и продажа сельхозпродукции", LegalType.PersonalSubsidiaryFarm, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q2.id, "Масштабируемый бизнес с привлечением инвестиций", LegalType.LLC, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q2.id, "Помощь уязвимым группам или решение социальных задач", LegalType.SocialEntrepreneur, 3))

        val q3 = LegalTypeSurveyQuestion(UUID.randomUUID(), "Как вы относитесь к отчётности и налогам?", 3)
        repository.createQuestion(q3)
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q3.id, "Хочу минимум отчётности и простой налог", LegalType.SelfEmployed, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q3.id, "Готов вести учёт в разумных пределах", LegalType.IndividualEntrepreneur, 2))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q3.id, "Готов оформлять хозяйство при необходимости", LegalType.PersonalSubsidiaryFarm, 2))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q3.id, "Готов к полному бухучёту и корпоративным процедурам", LegalType.LLC, 3))
        repository.createOption(LegalTypeSurveyOption(UUID.randomUUID(), q3.id, "Важна прозрачность для грантов и партнёров", LegalType.SocialEntrepreneur, 2))
    }
}
