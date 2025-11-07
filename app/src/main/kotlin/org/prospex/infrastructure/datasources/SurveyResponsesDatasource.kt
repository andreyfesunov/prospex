package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table
import org.prospex.infrastructure.datasources.SupportMeasuresDatasource.id
import java.util.*

object SurveyResponsesDatasource : Table("survey_responses") {
    val ideaId = uuid("id").entityId()
    val optionIds = array<UUID>("option_ids")

    override val primaryKey = PrimaryKey(id)
}