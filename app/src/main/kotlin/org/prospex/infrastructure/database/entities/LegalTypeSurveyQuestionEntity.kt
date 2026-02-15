package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.prospex.domain.models.LegalTypeSurveyQuestion
import java.util.UUID

@Entity(tableName = "legal_type_survey_questions")
data class LegalTypeSurveyQuestionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "order_index")
    var orderIndex: Int = 0
) {
    constructor() : this("", "", 0)
    companion object {
        fun fromDomain(q: LegalTypeSurveyQuestion) = LegalTypeSurveyQuestionEntity(
            q.id.toString(),
            q.text,
            q.order
        )
        fun toDomain(e: LegalTypeSurveyQuestionEntity) = LegalTypeSurveyQuestion(
            UUID.fromString(e.id),
            e.text,
            e.orderIndex
        )
    }
}
