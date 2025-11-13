package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "survey_responses",
    foreignKeys = [
        ForeignKey(
            entity = IdeaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idea_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SurveyResponseEntity(
    @PrimaryKey
    @ColumnInfo(name = "idea_id")
    var ideaId: String = "",
    @ColumnInfo(name = "option_ids")
    var optionIds: String = ""
) {
    constructor() : this("", "")
    companion object {
        fun fromDomain(ideaId: UUID, optionIds: Array<UUID>): SurveyResponseEntity {
            return SurveyResponseEntity(
                ideaId.toString(),
                optionIds.joinToString(",")
            )
        }
        
        fun toDomainOptionIds(optionIds: String): Array<UUID> {
            return optionIds.split(",").map { UUID.fromString(it) }.toTypedArray()
        }
    }
}

