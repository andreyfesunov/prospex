package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.prospex.domain.models.LegalTypeSurveyOption
import java.util.UUID

@Entity(
    tableName = "legal_type_survey_options",
    foreignKeys = [ForeignKey(
        entity = LegalTypeSurveyQuestionEntity::class,
        parentColumns = ["id"],
        childColumns = ["question_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("question_id")]
)
data class LegalTypeSurveyOptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "question_id")
    var questionId: String = "",
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "legal_type")
    var legalType: String = "",
    @ColumnInfo(name = "score")
    var score: Int = 0
) {
    constructor() : this("", "", "", "", 0)
    companion object {
        fun fromDomain(o: LegalTypeSurveyOption) = LegalTypeSurveyOptionEntity(
            o.id.toString(),
            o.questionId.toString(),
            o.text,
            o.legalType.name,
            o.score
        )
        fun toDomain(e: LegalTypeSurveyOptionEntity) = LegalTypeSurveyOption(
            UUID.fromString(e.id),
            UUID.fromString(e.questionId),
            e.text,
            org.prospex.domain.models.LegalType.valueOf(e.legalType),
            e.score
        )
    }
}
