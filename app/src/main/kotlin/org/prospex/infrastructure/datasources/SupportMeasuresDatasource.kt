package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table

object SupportMeasuresDatasource : Table("support_measures") {
    val id = uuid("id").entityId()
    val title = varchar("title", Constants.MAX_VARCHAR_LENGTH)
    val minScore = uinteger("min_score")

    override val primaryKey = PrimaryKey(id)
}