package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table

object UsersDatasource : Table("users") {
    val id = uuid("id").entityId()
    val email = varchar("email", Constants.MAX_VARCHAR_LENGTH).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}