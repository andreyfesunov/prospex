package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "question_options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["questionId"])]
)
data class QuestionOptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "questionId")
    var questionId: String = "",
    @ColumnInfo(name = "text")
    var text: String = "",
    @ColumnInfo(name = "score")
    var score: Int = 0
) {
    constructor() : this("", "", "", 0)
    companion object {
        fun fromDomain(
            id: UUID,
            questionId: UUID,
            text: String,
            score: UInt
        ): QuestionOptionEntity {
            return QuestionOptionEntity(
                id.toString(),
                questionId.toString(),
                text,
                score.toInt()
            )
        }
    }
}

