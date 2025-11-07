package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table
import org.prospex.domain.models.LegalType

object QuestionsDatasource : Table("questions") {
    val id = uuid("id").entityId()
    val text = varchar("text", Constants.MAX_VARCHAR_LENGTH)
    val legalType = enumeration<LegalType>("legal_type")

    override val primaryKey = PrimaryKey(id)
}