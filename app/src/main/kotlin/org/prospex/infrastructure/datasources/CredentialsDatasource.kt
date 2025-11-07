package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table

object CredentialsDatasource : Table("credentials") {
    val email = varchar("email", Constants.MAX_VARCHAR_LENGTH)
        .uniqueIndex()
        .references(ref = UsersDatasource.email)
        .entityId()
    val passwordHash = varchar("password_hash", Constants.MAX_VARCHAR_LENGTH)

    override val primaryKey = PrimaryKey(email)
}