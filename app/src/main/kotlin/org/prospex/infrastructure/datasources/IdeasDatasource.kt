package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table
import org.prospex.domain.models.LegalType

object IdeasDatasource : Table("ideas") {
    val id = uuid("id").entityId()
    val userId = uuid("user_id").references(ref = UsersDatasource.id)
    val title = varchar("title", Constants.MAX_VARCHAR_LENGTH)
    val description = varchar("description", Constants.MAX_VARCHAR_LENGTH)
    val legalType = enumeration<LegalType>("legal_type")
    val score = uinteger("score")

    override val primaryKey = PrimaryKey(id)
}