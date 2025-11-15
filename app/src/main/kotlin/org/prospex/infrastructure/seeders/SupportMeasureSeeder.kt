package org.prospex.infrastructure.seeders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.MeasureType
import org.prospex.domain.models.SupportMeasure
import org.prospex.domain.repositories.ISupportMeasureRepository
import java.util.UUID

class SupportMeasureSeeder(
    private val supportMeasureRepository: ISupportMeasureRepository
) {
    suspend fun seedIfEmpty() {
        withContext(Dispatchers.IO) {
            if (!supportMeasureRepository.hasAny()) {
                seedMeasures()
            }
        }
    }

    private suspend fun seedMeasures() {
        // Гранты
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Грант «Старт-ап»",
            measureType = MeasureType.Grant,
            legalTypes = arrayOf(LegalType.IndividualEntrepreneur, LegalType.LLC),
            amount = "до 3 000 000 ₽",
            features = "Требуется софинансирование от 20%. На запуск или развитие бизнеса.",
            covers = "Оборудование, сырье, аренду, маркетинг.",
            whereToApply = "Фонд развития предпринимательства Тульской области, портал «Мой бизнес 71»."
        ))

        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Грант по программе «Самозанятость»",
            measureType = MeasureType.Grant,
            legalTypes = arrayOf(LegalType.IndividualEntrepreneur, LegalType.SelfEmployed),
            amount = "~ 150 000 ₽ (пособие за 12 месяцев)",
            features = "Деньги выдаются единовременно после регистрации ИП или Самозанятого.",
            covers = "Стартовые затраты на открытие дела.",
            whereToApply = "Центр занятости населения (ЦЗН) по месту жительства."
        ))

        // Социальный контракт
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Соцконтракт на бизнес",
            measureType = MeasureType.SocialContract,
            legalTypes = arrayOf(LegalType.SelfEmployed, LegalType.IndividualEntrepreneur, LegalType.PersonalSubsidiaryFarm, LegalType.SocialEntrepreneur),
            amount = "до 350 000 ₽",
            features = "Необходим детальный бизнес-план. Деньги не возвращаются, но контролируется целевое использование.",
            covers = "Покупку оборудования, сырья, инвентаря для начала предпринимательской деятельности.",
            whereToApply = "Управление социальной защиты населения."
        ))

        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Соцконтракт на ЛПХ",
            measureType = MeasureType.SocialContract,
            legalTypes = arrayOf(LegalType.PersonalSubsidiaryFarm),
            amount = "до 200 000 ₽",
            features = "Направлен на развитие личного подсобного хозяйства.",
            covers = "Покупку скота, птицы, семян, саженцев, удобрений, теплиц.",
            whereToApply = "Управление социальной защиты населения."
        ))

        // Займы
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Микрозайм для МСП",
            measureType = MeasureType.Loan,
            legalTypes = arrayOf(LegalType.SelfEmployed, LegalType.IndividualEntrepreneur, LegalType.LLC),
            amount = "до 5 000 000 ₽",
            features = "Льготная ставка, упрощенная процедура получения по сравнению с банком (от 3% до 8% годовых).",
            covers = "Пополнение оборотных средств, закупка оборудования.",
            whereToApply = "Фонд микрофинансирования Тульской области."
        ))

        // Гарантии
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Гарантийная поддержка",
            measureType = MeasureType.Guarantee,
            legalTypes = arrayOf(LegalType.IndividualEntrepreneur, LegalType.LLC),
            amount = "до 70% от суммы кредита в банке",
            features = "Помогает получить кредит в банке, если не хватает залога. Фонд выступает поручителем.",
            covers = "Получение кредита на развитие бизнеса.",
            whereToApply = "Гарантийный фонд Тульской области."
        ))

        // Субсидии
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Субсидия на возмещение части затрат",
            measureType = MeasureType.Subsidy,
            legalTypes = arrayOf(LegalType.IndividualEntrepreneur, LegalType.LLC),
            amount = "до 500 000 – 1 500 000 ₽ (возмещает до 50-70% затрат)",
            features = "Выделяется после того, как вы сами потратили деньги и подтвердили это документами.",
            covers = "Лизинг оборудования, участие в выставках, подключение к онлайн-кассам, сертификацию, маркетинг.",
            whereToApply = "Фонд развития предпринимательства, портал «Мой бизнес 71»."
        ))

        // Нефинансовая поддержка
        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Консультации и обучение",
            measureType = MeasureType.NonFinancial,
            legalTypes = arrayOf(LegalType.SelfEmployed, LegalType.IndividualEntrepreneur, LegalType.LLC, LegalType.PersonalSubsidiaryFarm, LegalType.SocialEntrepreneur),
            amount = "Бесплатно",
            features = "Бесплатные консультации по юриспруденции, бухучету, налогам, маркетингу. Обучающие семинары и вебинары.",
            covers = "Консультации, обучение, семинары, вебинары.",
            whereToApply = "Центр «Мой бизнес», Бизнес-инкубатор."
        ))

        supportMeasureRepository.create(SupportMeasure(
            id = UUID.randomUUID(),
            title = "Инфраструктурная поддержка",
            measureType = MeasureType.NonFinancial,
            legalTypes = arrayOf(LegalType.IndividualEntrepreneur, LegalType.LLC),
            amount = "Льготная аренда",
            features = "Предоставление помещений в аренду по льготной ставке в бизнес-инкубаторах, коворкингах.",
            covers = "Аренда помещений в бизнес-инкубаторах и коворкингах.",
            whereToApply = "Бизнес-инкубатор Тульской области."
        ))
    }
}

