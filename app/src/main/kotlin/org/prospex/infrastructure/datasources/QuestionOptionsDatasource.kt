package org.prospex.infrastructure.datasources

import org.jetbrains.exposed.v1.core.Table

object QuestionOptionsDatasource : Table("question_options") {
    val id = uuid("id").entityId()
    val questionId = uuid("questionId").references(ref = QuestionsDatasource.id)
    val text = varchar("text", Constants.MAX_VARCHAR_LENGTH)
    val score = uinteger("score")

    override val primaryKey = PrimaryKey(id)
}