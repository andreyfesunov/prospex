package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "legal_type")
    var legalType: String = "",
    @ColumnInfo(name = "type")
    var type: String = ""
) {
    constructor() : this("", "", "", "")
    companion object {
        fun fromDomain(
            id: UUID,
            text: String,
            legalType: String,
            type: String
        ): QuestionEntity {
            return QuestionEntity(
                id.toString(),
                text,
                legalType,
                type
            )
        }
    }
}

