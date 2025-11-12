package org.prospex.infrastructure.seeders

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.Question
import org.prospex.domain.models.QuestionOption
import org.prospex.domain.models.QuestionType
import org.prospex.domain.repositories.ISurveyRepository
import org.prospex.domain.value_objects.Score
import java.util.UUID

class QuestionSeeder(
    private val surveyRepository: ISurveyRepository
) {
    suspend fun seedIfEmpty() {
        withContext(Dispatchers.IO) {
            if (!surveyRepository.hasAnyOptions()) {
                seedQuestions()
            }
        }
    }

    private suspend fun seedQuestions() {
        seedSelfEmployed()
        seedIndividualEntrepreneur()
        seedPersonalSubsidiaryFarm()
        seedLLC()
        seedSocialEntrepreneur()
    }

    private suspend fun seedSocialEntrepreneur() {
        val q1_1 = Question(
            id = UUID.randomUUID(),
            text = "Какая у вас бизнес-модель?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Полностью зависим от грантов и пожертвований",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Смешанная модель (часть доходов от деятельности, часть — субсидии)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Самоокупаемая бизнес-модель, где >70% доходов генерируется от продажи товаров/услуг",
            score = Score(5u)
        ))

        val q1_2 = Question(
            id = UUID.randomUUID(),
            text = "Насколько детально просчитана финансовая модель?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Есть только общая смета расходов",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Составлен бюджет на первый год с учетом социальных затрат",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Разработана полноценная финмодель на 3 года, отдельно учитывающая социальные и коммерческие расходы/доходы",
            score = Score(10u)
        ))

        val q1_3 = Question(
            id = UUID.randomUUID(),
            text = "Какова доля собственных/привлеченных средств в стартовых затратах?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "0%",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Менее 30%",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "30% и более",
            score = Score(10u)
        ))

        val q1_4 = Question(
            id = UUID.randomUUID(),
            text = "Каков план по достижению финансовой самоокупаемости?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "Плана нет, надеемся на постоянное финансирование",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "План есть, срок окупаемости более 3 лет",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "Четкий план с выходом на самоокупаемость в течение 1-3 лет и показателями контроля",
            score = Score(10u)
        ))

        val q2_1 = Question(
            id = UUID.randomUUID(),
            text = "Насколько точно определена и измеряема ваша целевая социальная группа?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Люди, нуждающиеся в помощи",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Определена группа (например, «дети с РАС»)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Четко определена и измерима (например, «30 детей с РАС в возрасте 5-7 лет в Зареченском районе Тулы»)",
            score = Score(5u)
        ))

        val q2_2 = Question(
            id = UUID.randomUUID(),
            text = "Как вы будете измерять и оценивать социальные результаты? (KPI воздействия)",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Не планируем измерять / измерением будет факт нашей работы",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Будем вести количественный учет (число обслуженных)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Есть система качественных и количественных KPI (например, % трудоустроенных инвалидов, уровень улучшения навыков)",
            score = Score(5u)
        ))

        val q2_3 = Question(
            id = UUID.randomUUID(),
            text = "Какую конкретную социальную проблему и на каком уровне вы решаете?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Помощь людям",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Решаем локальную проблему (нехватка досуга для пенсионеров в микрорайоне)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Решаем системную проблему на уровне города/области (трудоустройство слабослышащих, создание инклюзивной среды)",
            score = Score(5u)
        ))

        val q2_4 = Question(
            id = UUID.randomUUID(),
            text = "Как ваш проект интегрирует целевую группу в экономику и общество?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Оказываем разовую или патронажную помощь",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Обеспечиваем занятость или социализацию",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Обучаем навыкам, трудоустраиваем, создаем для них продукты/услуги, меняем общественное мнение",
            score = Score(5u)
        ))

        val q2_5 = Question(
            id = UUID.randomUUID(),
            text = "Есть ли у вас партнеры (НКО, госучреждения, бизнес) для усиления воздействия?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_5)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Есть договоренности о сотрудничестве",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Выстроена партнерская сеть для реализации проекта (ЦЗН направляет клиентов, бизнес предоставляет стажировки)",
            score = Score(5u)
        ))

        val q3_1 = Question(
            id = UUID.randomUUID(),
            text = "Какова ваша основная ценность для «платящих» клиентов?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "У нас нет платящих клиентов, все услуги бесплатны для beneficiaries",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Ценность в качестве услуги/товара (например, качественная кондитерская, где работают инвалиды)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Двойная ценность: качественный продукт + социальный вклад (потребитель участвует в благом деле, покупая товар)",
            score = Score(5u)
        ))

        val q3_2 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли каналы привлечения клиентов/потребителей?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Будем использовать стандартные каналы (соцсети, реклама)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Есть стратегия, использующая социальную миссию как маркетинговый актив (истории impact, сотрудничество с СМИ)",
            score = Score(5u)
        ))

        val q3_3 = Question(
            id = UUID.randomUUID(),
            text = "Решены ли вопросы с помещением, оборудованием и инфраструктурой, доступной для всех?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Помещение есть, но требует адаптации",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Да, помещение подобрано с учетом требований доступной среды (пандусы, тактильные таблички и т.д.)",
            score = Score(5u)
        ))

        val q3_4 = Question(
            id = UUID.randomUUID(),
            text = "Разработана ли организационная структура и прописаны ли роли?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Нет, все делаем вместе",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Есть понимание ключевых сотрудников",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Прописаны зоны ответственности, в том числе для сотрудников из целевых групп (сопровождаемый труд)",
            score = Score(5u)
        ))

        val q4_1 = Question(
            id = UUID.randomUUID(),
            text = "Есть ли в команде сочетание предпринимательских и социальных компетенций?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Команда состоит только из социальных работников или только из бизнесменов",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Есть консультанты с противоположной экспертизой",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Сбалансированная команда, где есть и те, кто понимает бизнес, и те, кто глубоко погружен в социальную проблему",
            score = Score(10u)
        ))

        val q4_2 = Question(
            id = UUID.randomUUID(),
            text = "Есть ли понимание правового регулирования (в т.ч. получение статуса соцпредприятия)?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "В общих чертах знаем о требованиях",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Да, мы изучили реестр Минэкономразвития и критерии для включения в него",
            score = Score(5u)
        ))

        val q5_1 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли риски (репутационные, зависимость от ключевого сотрудника-энтузиаста, выгорание)?",
            legalType = LegalType.SocialEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q5_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Основные риски идентифицированы",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Разработаны меры по mitigation ключевых рисков, есть план тиражирования модели в другие районы",
            score = Score(5u)
        ))
    }

    private suspend fun seedLLC() {
            val q1_1 = Question(
                id = UUID.randomUUID(),
                text = "Разработана ли детальная финансовая модель на первые 2-3 года?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q1_1)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_1.id,
                text = "Нет, есть только общие расчеты",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_1.id,
                text = "Есть прогноз по основным показателям (выручка, затраты) на 1 год",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_1.id,
                text = "Да, есть полноценная модель с прогнозом движения денежных средств, отчетом о прибылях и убытках и балансом",
                score = Score(5u)
            ))

            val q1_2 = Question(
                id = UUID.randomUUID(),
                text = "Какова структура финансирования стартовых затрат?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q1_2)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_2.id,
                text = "100% заемные средства",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_2.id,
                text = "Собственные средства учредителей составляют менее 30%",
                score = Score(5u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_2.id,
                text = "Собственные средства учредителей составляют 30% и более",
                score = Score(10u)
            ))

            val q1_3 = Question(
                id = UUID.randomUUID(),
                text = "Рассчитаны ли ключевые финансовые показатели (точка безубыточности, ROI, рентабельность)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q1_3)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_3.id,
                text = "Нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_3.id,
                text = "Рассчитана точка безубыточности",
                score = Score(5u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_3.id,
                text = "Рассчитаны и точк безубыточности, и ROI (>20%), и рентабельность продаж/производства",
                score = Score(10u)
            ))

            val q1_4 = Question(
                id = UUID.randomUUID(),
                text = "Каков планируемый срок окупаемости инвестиций (PP) и внутренняя норма доходности (IRR)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q1_4)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_4.id,
                text = "Срок окупаемости более 5 лет / не рассчитывался",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_4.id,
                text = "PP от 2 до 5 лет",
                score = Score(5u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q1_4.id,
                text = "PP менее 2 лет, IRR превышает ставку по альтернативным инвестициям",
                score = Score(10u)
            ))

            val q2_1 = Question(
                id = UUID.randomUUID(),
                text = "Проведено ли углубленное маркетинговое исследование (объем рынка, динамика, ключевые игроки)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q2_1)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_1.id,
                text = "Нет, опираемся на общее понимание рынка",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_1.id,
                text = "Проведен самостоятельный анализ по открытым источникам",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_1.id,
                text = "Да, проведено структурированное исследование, результаты задокументированы",
                score = Score(5u)
            ))

            val q2_2 = Question(
                id = UUID.randomUUID(),
                text = "Сформулирована ли конкурентная стратегия компании?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q2_2)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_2.id,
                text = "Будем конкурировать по цене",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_2.id,
                text = "Есть понимание своей ниши и УТП",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_2.id,
                text = "Четко определена стратегия (лидерство по издержкам, дифференциация, фокус на сегменте) и план ее реализации",
                score = Score(5u)
            ))

            val q2_3 = Question(
                id = UUID.randomUUID(),
                text = "Разработан ли комплексный маркетинговый и коммерческий план?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q2_3)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_3.id,
                text = "Плана нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_3.id,
                text = "Есть план по привлечению клиентов",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_3.id,
                text = "Есть интегрированный план, включающий ценообразование, продвижение, дистрибуцию и продажи с KPI",
                score = Score(5u)
            ))

            val q2_4 = Question(
                id = UUID.randomUUID(),
                text = "Насколько бизнес-модель устойчива к кризисам и изменению конъюнктуры?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q2_4)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_4.id,
                text = "Бизнес-модель не проработана на устойчивость",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_4.id,
                text = "Есть диверсифицированная клиентская база",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_4.id,
                text = "Модель устойчива (диверсификация продуктов/услуг, долгосрочные контракты, сильный бренд)",
                score = Score(5u)
            ))

            val q2_5 = Question(
                id = UUID.randomUUID(),
                text = "Какой вклад вносит проект в экономику региона (налоги, импортозамещение, кооперация)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q2_5)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_5.id,
                text = "Вклад минимален",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_5.id,
                text = "Создание новых рабочих мест",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q2_5.id,
                text = "Значительный вклад (высокотехнологичное производство, экспорт, развитие смежных отраслей)",
                score = Score(5u)
            ))

            val q3_1 = Question(
                id = UUID.randomUUID(),
                text = "Проработана ли вся операционная цепочка (снабжение, производство, логистика, контроль качества)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q3_1)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_1.id,
                text = "Нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_1.id,
                text = "Ключевые процессы определены",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_1.id,
                text = "Все процессы детально прописаны, определены ответственные и точки контроля",
                score = Score(5u)
            ))

            val q3_2 = Question(
                id = UUID.randomUUID(),
                text = "Решены ли вопросы с производственными площадями, оборудованием и его обслуживанием?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q3_2)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_2.id,
                text = "Нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_2.id,
                text = "Есть понимание требований и варианты",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_2.id,
                text = "Подобрано/зарезервировано помещение, подобран поставщик оборудования, заключены предварительные договоры",
                score = Score(5u)
            ))

            val q3_3 = Question(
                id = UUID.randomUUID(),
                text = "Разработана ли организационная структура и штатное расписание?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q3_3)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_3.id,
                text = "Нет, все будут делать всё",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_3.id,
                text = "Есть примерный список ключевых сотрудников",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_3.id,
                text = "Разработана оргструктура, штатное расписание и понятны функции каждого подразделения",
                score = Score(5u)
            ))

            val q3_4 = Question(
                id = UUID.randomUUID(),
                text = "Есть ли план по обеспечению необходимыми лицензиями, сертификатами и патентами?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q3_4)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_4.id,
                text = "Нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_4.id,
                text = "Определен перечень необходимых документов",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q3_4.id,
                text = "Разработан план и график получения всех разрешительных документов",
                score = Score(5u)
            ))

            val q4_1 = Question(
                id = UUID.randomUUID(),
                text = "Сбалансирована ли команда учредителей (есть компетенции в менеджменте, финансах, продажах, производстве)?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q4_1)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_1.id,
                text = "Команда состоит из людей с одинаковыми навыками",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_1.id,
                text = "Есть дополняющие друг друга компетенции",
                score = Score(5u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_1.id,
                text = "Команда сбалансирована и имеет успешный опыт реализации подобных проектов",
                score = Score(10u)
            ))

            val q4_2 = Question(
                id = UUID.randomUUID(),
                text = "Разработана ли система юридического и бухгалтерского сопровождения?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q4_2)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_2.id,
                text = "Нет, будем разбираться по ходу",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_2.id,
                text = "Планируем нанять бухгалтера",
                score = Score(3u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q4_2.id,
                text = "Определена структура (штатный юрист/бухгалтер или аутсорсинговая фирма)",
                score = Score(5u)
            ))

            val q5_1 = Question(
                id = UUID.randomUUID(),
                text = "Разработан ли комплексный план по управлению рисками (финансовыми, операционными, рыночными) и есть ли стратегия выхода?",
                legalType = LegalType.LLC,
                type = QuestionType.Radio
            )
            surveyRepository.create(q5_1)
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q5_1.id,
                text = "Нет",
                score = Score(0u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q5_1.id,
                text = "Основные риски идентифицированы",
                score = Score(2u)
            ))
            surveyRepository.create(QuestionOption(
                id = UUID.randomUUID(),
                questionId = q5_1.id,
                text = "Существует матрица рисков с прописанными мерами по их mitigation, есть понимание стратегии выхода (продажа, IPO)",
                score = Score(5u)
            ))
        }

    private suspend fun seedPersonalSubsidiaryFarm() {
        val q1_1 = Question(
            id = UUID.randomUUID(),
            text = "Насколько детально вы просчитали стартовые инвестиции?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Есть только общая приблизительная сумма",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Составлен список основных статей расходов (оборудование, ремонт, товар)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Составлена детальная смета по всем статьям, включая непредвиденные расходы (до 10%)",
            score = Score(5u)
        ))

        val q1_2 = Question(
            id = UUID.randomUUID(),
            text = "Какова доля ваших собственных средств в общем объеме инвестиций?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "0%",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Менее 20%",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "От 20% до 50%",
            score = Score(7u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Более 50%",
            score = Score(10u)
        ))

        val q1_3 = Question(
            id = UUID.randomUUID(),
            text = "Рассчитан ли у вас план движения денежных средств (Cash Flow) на первые 6-12 месяцев?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Есть примерный расчет по месяцам",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Да, есть помесячный план с учетом сезонности, налогов и всех регулярных платежей",
            score = Score(10u)
        ))

        val q1_4 = Question(
            id = UUID.randomUUID(),
            text = "Каков расчетный срок окупаемости проекта?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "Более 24 месяцев",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "12-24 месяца",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "6-12 месяцев",
            score = Score(7u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "До 6 месяцев",
            score = Score(10u)
        ))

        val q2_1 = Question(
            id = UUID.randomUUID(),
            text = "Сформулировано ли ваше Уникальное Торговое Предложение (УТП)?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Отсутствует, работаю в общем потоке",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Есть, но слабо выражено (например, «хорошее качество»)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Четкое и измеримое УТП (например, «единственная пекарня в районе на безглютеновой закваске»)",
            score = Score(5u)
        ))

        val q2_2 = Question(
            id = UUID.randomUUID(),
            text = "Проводили ли вы анализ конкурентов (цены, ассортимент, слабые стороны)?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Поверхностно, ознакомился с ценами",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Да, проведен детальный анализ, результаты зафиксированы",
            score = Score(5u)
        ))

        val q2_3 = Question(
            id = UUID.randomUUID(),
            text = "Какой у вас план продвижения и рекламный бюджет на первые 3 месяца?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Плана и бюджета нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Есть план, но без бюджета (только бесплатные методы)",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Есть структурированный план с распределением бюджета по каналам (соцсети, наружка, контекст)",
            score = Score(5u)
        ))

        val q2_4 = Question(
            id = UUID.randomUUID(),
            text = "Определены ли ваши каналы сбыта?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Не определены",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Один основной канал (например, собственная торговая точка)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Несколько каналов (торговая точка + онлайн-продажи + сотрудничество с другими магазинами)",
            score = Score(5u)
        ))

        val q2_5 = Question(
            id = UUID.randomUUID(),
            text = "Насколько ваш бизнес социально значим для муниципалитета?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_5)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Не значим",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Создает новые рабочие места / решает бытовые проблемы",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Восполняет острый дефицит товаров/услуг в районе / является импортозамещающим",
            score = Score(5u)
        ))

        val q3_1 = Question(
            id = UUID.randomUUID(),
            text = "Решен ли вопрос с помещением?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Есть варианты на стадии просмотра",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Да, помещение подобрано / есть договор аренды / в собственности",
            score = Score(5u)
        ))

        val q3_2 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли цепочки поставок сырья/товаров?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Есть 1 основной поставщик",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Есть несколько проверенных поставщиков для ключевых позиций, известны условия",
            score = Score(5u)
        ))

        val q3_3 = Question(
            id = UUID.randomUUID(),
            text = "Планируете ли вы нанимать сотрудников?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Нет, буду работать один(на)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Да, от 1 до 3 сотрудников",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Да, от 4 и более сотрудников",
            score = Score(5u)
        ))

        val q3_4 = Question(
            id = UUID.randomUUID(),
            text = "Выбран ли режим налогообложения и понимаете ли вы налоговую нагрузку?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Выбрал УСН «Доходы», но нагрузку не считал",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Да, выбран режим (УСН, Патент) и рассчитана примерная налоговая нагрузка",
            score = Score(5u)
        ))

        val q4_1 = Question(
            id = UUID.randomUUID(),
            text = "Наличие профильного опыта и образования.",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Опыта и образования в этой сфере нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Есть опыт смежной деятельности или прошел курсы",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Есть профильное образование и/или опыт работы в этой сфере более 3 лет",
            score = Score(10u)
        ))

        val q4_2 = Question(
            id = UUID.randomUUID(),
            text = "Готовы ли вы к полному документообороту (касса, отчетность в налоговую, договоры)?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Нет, не готов разбираться",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Готов(а) частично, буду пользоваться услугами бухгалтера",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Да, я понимаю объем обязанностей и готов(а) к их выполнению (самостоятельно или с аутсорсом)",
            score = Score(5u)
        ))

        val q5_1 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли основные риски (падение спроса, рост цен, проблемы с поставками)?",
            legalType = LegalType.PersonalSubsidiaryFarm,
            type = QuestionType.Radio
        )
        surveyRepository.create(q5_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Риски не проработаны",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Риски идентифицированы, но планов по минимизации нет",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Для ключевых рисков разработаны конкретные меры по их снижению (например, диверсификация поставщиков)",
            score = Score(5u)
        ))
    }

    private suspend fun seedIndividualEntrepreneur() {
        val q1_1 = Question(
            id = UUID.randomUUID(),
            text = "Насколько детально вы просчитали стартовые инвестиции?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Есть только общая приблизительная сумма",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Составлен список основных статей расходов (оборудование, ремонт, товар)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_1.id,
            text = "Составлена детальная смета по всем статьям, включая непредвиденные расходы (до 10%)",
            score = Score(5u)
        ))

        val q1_2 = Question(
            id = UUID.randomUUID(),
            text = "Какова доля ваших собственных средств в общем объеме инвестиций?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "0%",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Менее 20%",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "От 20% до 50%",
            score = Score(7u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_2.id,
            text = "Более 50%",
            score = Score(10u)
        ))

        val q1_3 = Question(
            id = UUID.randomUUID(),
            text = "Рассчитан ли у вас план движения денежных средств (Cash Flow) на первые 6-12 месяцев?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Есть примерный расчет по месяцам",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_3.id,
            text = "Да, есть помесячный план с учетом сезонности, налогов и всех регулярных платежей",
            score = Score(10u)
        ))

        val q1_4 = Question(
            id = UUID.randomUUID(),
            text = "Каков расчетный срок окупаемости проекта?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q1_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "Более 24 месяцев",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "12-24 месяца",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "6-12 месяцев",
            score = Score(7u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q1_4.id,
            text = "До 6 месяцев",
            score = Score(10u)
        ))

        val q2_1 = Question(
            id = UUID.randomUUID(),
            text = "Сформулировано ли ваше Уникальное Торговое Предложение (УТП)?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Отсутствует, работаю в общем потоке",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Есть, но слабо выражено (например, «хорошее качество»)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_1.id,
            text = "Четкое и измеримое УТП (например, «единственная пекарня в районе на безглютеновой закваске»)",
            score = Score(5u)
        ))

        val q2_2 = Question(
            id = UUID.randomUUID(),
            text = "Проводили ли вы анализ конкурентов (цены, ассортимент, слабые стороны)?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Поверхностно, ознакомился с ценами",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_2.id,
            text = "Да, проведен детальный анализ, результаты зафиксированы",
            score = Score(5u)
        ))

        val q2_3 = Question(
            id = UUID.randomUUID(),
            text = "Какой у вас план продвижения и рекламный бюджет на первые 3 месяца?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Плана и бюджета нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Есть план, но без бюджета (только бесплатные методы)",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_3.id,
            text = "Есть структурированный план с распределением бюджета по каналам (соцсети, наружка, контекст)",
            score = Score(5u)
        ))

        val q2_4 = Question(
            id = UUID.randomUUID(),
            text = "Определены ли ваши каналы сбыта?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Не определены",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Один основной канал (например, собственная торговая точка)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_4.id,
            text = "Несколько каналов (торговая точка + онлайн-продажи + сотрудничество с другими магазинами)",
            score = Score(5u)
        ))

        val q2_5 = Question(
            id = UUID.randomUUID(),
            text = "Насколько ваш бизнес социально значим для муниципалитета?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q2_5)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Не значим",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Создает новые рабочие места / решает бытовые проблемы",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q2_5.id,
            text = "Восполняет острый дефицит товаров/услуг в районе / является импортозамещающим",
            score = Score(5u)
        ))

        val q3_1 = Question(
            id = UUID.randomUUID(),
            text = "Решен ли вопрос с помещением?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Есть варианты на стадии просмотра",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_1.id,
            text = "Да, помещение подобрано / есть договор аренды / в собственности",
            score = Score(5u)
        ))

        val q3_2 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли цепочки поставок сырья/товаров?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Есть 1 основной поставщик",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_2.id,
            text = "Есть несколько проверенных поставщиков для ключевых позиций, известны условия",
            score = Score(5u)
        ))

        val q3_3 = Question(
            id = UUID.randomUUID(),
            text = "Планируете ли вы нанимать сотрудников?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Нет, буду работать один(на)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Да, от 1 до 3 сотрудников",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_3.id,
            text = "Да, от 4 и более сотрудников",
            score = Score(5u)
        ))

        val q3_4 = Question(
            id = UUID.randomUUID(),
            text = "Выбран ли режим налогообложения и понимаете ли вы налоговую нагрузку?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q3_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Выбрал УСН «Доходы», но нагрузку не считал",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q3_4.id,
            text = "Да, выбран режим (УСН, Патент) и рассчитана примерная налоговая нагрузка",
            score = Score(5u)
        ))

        val q4_1 = Question(
            id = UUID.randomUUID(),
            text = "Наличие профильного опыта и образования.",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Опыта и образования в этой сфере нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Есть опыт смежной деятельности или прошел курсы",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_1.id,
            text = "Есть профильное образование и/или опыт работы в этой сфере более 3 лет",
            score = Score(10u)
        ))

        val q4_2 = Question(
            id = UUID.randomUUID(),
            text = "Готовы ли вы к полному документообороту (касса, отчетность в налоговую, договоры)?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q4_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Нет, не готов разбираться",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Готов(а) частично, буду пользоваться услугами бухгалтера",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q4_2.id,
            text = "Да, я понимаю объем обязанностей и готов(а) к их выполнению (самостоятельно или с аутсорсом)",
            score = Score(5u)
        ))

        val q5_1 = Question(
            id = UUID.randomUUID(),
            text = "Проработаны ли основные риски (падение спроса, рост цен, проблемы с поставками)?",
            legalType = LegalType.IndividualEntrepreneur,
            type = QuestionType.Radio
        )
        surveyRepository.create(q5_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Риски не проработаны",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Риски идентифицированы, но планов по минимизации нет",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = q5_1.id,
            text = "Для ключевых рисков разработаны конкретные меры по их снижению (например, диверсификация поставщиков)",
            score = Score(5u)
        ))
    }

    private suspend fun seedSelfEmployed() {
        val question1_1 = Question(
            id = UUID.randomUUID(),
            text = "Оцените свои стартовые затраты.",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question1_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_1.id,
            text = "Не рассчитывал(а)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_1.id,
            text = "Есть приблизительная оценка",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_1.id,
            text = "Составил(а) детальный список необходимого с ценами",
            score = Score(5u)
        ))

        val question1_2 = Question(
            id = UUID.randomUUID(),
            text = "Какую сумму собственных средств вы готовы вложить?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question1_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_2.id,
            text = "0 рублей",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_2.id,
            text = "Менее 30% от требуемой суммы",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_2.id,
            text = "30% и более от требуемой суммы",
            score = Score(10u)
        ))

        val question1_3 = Question(
            id = UUID.randomUUID(),
            text = "Как скоро вы планируете выйти на ежемесячную чистую прибыль?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question1_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_3.id,
            text = "Более чем через 6 месяцев",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_3.id,
            text = "Через 3-6 месяцев",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_3.id,
            text = "В течение первых 1-2 месяцев",
            score = Score(10u)
        ))

        val question1_4 = Question(
            id = UUID.randomUUID(),
            text = "Какой размер среднемесячной чистой прибыли вы ожидаете после выхода на стабильную работу?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question1_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_4.id,
            text = "Менее 15 000 рублей",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_4.id,
            text = "От 15 000 до 40 000 рублей",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question1_4.id,
            text = "Более 40 000 рублей",
            score = Score(10u)
        ))

        val question2_1 = Question(
            id = UUID.randomUUID(),
            text = "Что делает ваше предложение уникальным? (Ваше УТП)",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question2_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_1.id,
            text = "Ничего, я буду делать как все / Не задумывался(ась)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_1.id,
            text = "Есть небольшие отличия (удобное время, расположение)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_1.id,
            text = "Есть четкое уникальное преимущество (особая технология, редкая услуга, уникальный стиль)",
            score = Score(5u)
        ))

        val question2_2 = Question(
            id = UUID.randomUUID(),
            text = "Насколько хорошо вы изучили своих конкурентов?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question2_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_2.id,
            text = "Не изучал(а)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_2.id,
            text = "Знаю, что они есть",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_2.id,
            text = "Знаю их цены, сильные и слабые стороны",
            score = Score(5u)
        ))

        val question2_3 = Question(
            id = UUID.randomUUID(),
            text = "Как вы планируете привлекать первых клиентов? (отметьте все подходящие варианты)",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Checkbox
        )
        surveyRepository.create(question2_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_3.id,
            text = "Сарафанное радио",
            score = Score(1u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_3.id,
            text = "Социальные сети (без бюджета на рекламу)",
            score = Score(1u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_3.id,
            text = "Площадки объявлений (Авито, Юла и т.д.)",
            score = Score(1u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_3.id,
            text = "Есть план с конкретными шагами и, возможно, небольшим бюджетом на рекламу",
            score = Score(2u)
        ))

        val question2_4 = Question(
            id = UUID.randomUUID(),
            text = "Кто ваша целевая аудитория?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question2_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_4.id,
            text = "Все",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_4.id,
            text = "Знаю общие черты (например, \"женщины 25-40 лет\")",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_4.id,
            text = "Могу описать конкретный портрет (например, \"молодые мамы в моем районе, которые ценят время\")",
            score = Score(5u)
        ))

        val question2_5 = Question(
            id = UUID.randomUUID(),
            text = "Насколько ваша услуга социально значима для вашего района/города?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question2_5)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_5.id,
            text = "Не значима, это чисто коммерческий проект",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_5.id,
            text = "Решает небольшую бытовую проблему (ремонт, уборка)",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question2_5.id,
            text = "Восполняет дефицит важных услуг в районе (репетиторство для детей, услуги сиделки, редкая ремесленная услуга)",
            score = Score(5u)
        ))

        val question3_1 = Question(
            id = UUID.randomUUID(),
            text = "Насколько готовы ключевые ресурсы для старта?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question3_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_1.id,
            text = "Ничего нет, только идея",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_1.id,
            text = "Есть часть оборудования или инструментов",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_1.id,
            text = "Есть все необходимое оборудование и материалы для начала работы",
            score = Score(5u)
        ))

        val question3_2 = Question(
            id = UUID.randomUUID(),
            text = "Где вы будете оказывать услуги?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question3_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_2.id,
            text = "Не определился(ась)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_2.id,
            text = "На выезде к клиенту (без постоянного места)",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_2.id,
            text = "Есть подготовленное место (домашняя мастерская/кабинет, арендованное помещение)",
            score = Score(5u)
        ))

        val question3_3 = Question(
            id = UUID.randomUUID(),
            text = "Есть ли у вас надежные каналы для закупки материалов/расходников?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question3_3)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_3.id,
            text = "Нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_3.id,
            text = "Есть предполагаемые поставщики",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_3.id,
            text = "Да, я уже нашел конкретных поставщиков и знаю цены",
            score = Score(5u)
        ))

        val question3_4 = Question(
            id = UUID.randomUUID(),
            text = "Насколько точно вы просчитали себестоимость вашей услуги?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question3_4)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_4.id,
            text = "Не просчитывал(а)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_4.id,
            text = "Знаю примерную стоимость",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question3_4.id,
            text = "Знаю точную стоимость с учетом всех расходников, амортизации и времени",
            score = Score(5u)
        ))

        val question4_1 = Question(
            id = UUID.randomUUID(),
            text = "Насколько ваш опыт и/или образование связаны с этой деятельностью?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question4_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_1.id,
            text = "Опыта и образования нет",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_1.id,
            text = "Есть хобби-уровень или краткосрочные курсы",
            score = Score(5u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_1.id,
            text = "Есть профильное образование и/или подтвержденный опыт работы в этой сфере",
            score = Score(10u)
        ))

        val question4_2 = Question(
            id = UUID.randomUUID(),
            text = "Готовы ли вы к ведению учета и документооборота (чеки, отчеты для соцзащиты)?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question4_2)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_2.id,
            text = "Нет, не готов(а)",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_2.id,
            text = "Готов(а) научиться",
            score = Score(3u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question4_2.id,
            text = "Да, я понимаю necessity и готов(а) к этому",
            score = Score(5u)
        ))

        val question5_1 = Question(
            id = UUID.randomUUID(),
            text = "Какие главные риски вы видите для своего бизнеса и как планируете с ними бороться?",
            legalType = LegalType.SelfEmployed,
            type = QuestionType.Radio
        )
        surveyRepository.create(question5_1)
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question5_1.id,
            text = "Рисков не вижу / не думал(а) об этом",
            score = Score(0u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question5_1.id,
            text = "Вижу риски (например, \"не будет клиентов\"), но плана по их преодолению нет",
            score = Score(2u)
        ))
        surveyRepository.create(QuestionOption(
            id = UUID.randomUUID(),
            questionId = question5_1.id,
            text = "Я определил(а) ключевые риски (спрос, поломка инструмента) и у меня есть план действий для каждого",
            score = Score(5u)
        ))
    }
}

