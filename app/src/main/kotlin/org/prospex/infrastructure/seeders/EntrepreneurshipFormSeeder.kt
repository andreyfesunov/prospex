package org.prospex.infrastructure.seeders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.prospex.domain.models.EntrepreneurshipForm
import org.prospex.domain.models.LegalType
import org.prospex.domain.repositories.IEntrepreneurshipFormRepository
import java.util.UUID

class EntrepreneurshipFormSeeder(
    private val repository: IEntrepreneurshipFormRepository
) {
    suspend fun seedIfEmpty() {
        withContext(Dispatchers.IO) {
            if (!repository.hasAny()) {
                seedForms()
            }
        }
    }

    private suspend fun seedForms() {
        LegalType.entries.forEach { legalType ->
            repository.create(
                EntrepreneurshipForm(
                    id = UUID.randomUUID(),
                    title = getTitle(legalType),
                    legalType = legalType,
                    description = getDescription(legalType),
                    features = getFeatures(legalType),
                    requirements = getRequirements(legalType)
                )
            )
        }
    }

    private fun getTitle(legalType: LegalType): String = when (legalType) {
        LegalType.SelfEmployed -> "Самозанятый"
        LegalType.IndividualEntrepreneur -> "Индивидуальный предприниматель (ИП)"
        LegalType.PersonalSubsidiaryFarm -> "Личное подсобное хозяйство (ЛПХ)"
        LegalType.LLC -> "Общество с ограниченной ответственностью (ООО)"
        LegalType.SocialEntrepreneur -> "Социальный предприниматель"
    }

    private fun getDescription(legalType: LegalType): String = when (legalType) {
        LegalType.SelfEmployed -> "Специальный налоговый режим для физических лиц, осуществляющих доходную деятельность без работодателя и наёмных сотрудников."
        LegalType.IndividualEntrepreneur -> "Форма ведения бизнеса, при которой предприниматель действует от своего имени без образования юридического лица."
        LegalType.PersonalSubsidiaryFarm -> "Форма непредпринимательской деятельности по производству и переработке сельскохозяйственной продукции на земельном участке."
        LegalType.LLC -> "Коммерческая организация, уставный капитал которой разделён на доли; участники не отвечают по обязательствам общества."
        LegalType.SocialEntrepreneur -> "Предпринимательская деятельность, направленная на достижение социальных целей и решение социальных проблем общества."
    }

    private fun getFeatures(legalType: LegalType): String = when (legalType) {
        LegalType.SelfEmployed -> "Налог 4–6%, упрощённая отчётность, регистрация через приложение «Мой налог»."
        LegalType.IndividualEntrepreneur -> "Упрощённая регистрация, возможность применять УСН, ПСН, патент."
        LegalType.PersonalSubsidiaryFarm -> "Не требуется регистрация в качестве ИП при продаже излишков; льготы по земельному налогу."
        LegalType.LLC -> "Ограниченная ответственность участников, возможность привлечения инвестиций, корпоративное управление."
        LegalType.SocialEntrepreneur -> "Социальная миссия, возможность получения грантов и целевой поддержки, реестр социальных предпринимателей."
    }

    private fun getRequirements(legalType: LegalType): String = when (legalType) {
        LegalType.SelfEmployed -> "Гражданство РФ, отсутствие наёмных сотрудников, годовой доход до 2,4 млн ₽."
        LegalType.IndividualEntrepreneur -> "Дееспособность, регистрация в ЕГРИП, выбор системы налогообложения."
        LegalType.PersonalSubsidiaryFarm -> "Земельный участок для ведения ЛПХ, площадь не превышает установленные нормы."
        LegalType.LLC -> "Уставный капитал от 10 000 ₽, учредительные документы, государственная регистрация в ЕГРЮЛ."
        LegalType.SocialEntrepreneur -> "Соответствие критериям социального предпринимательства, деятельность в приоритетных сферах."
    }
}
