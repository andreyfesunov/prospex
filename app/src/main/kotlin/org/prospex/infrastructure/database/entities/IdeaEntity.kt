package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.prospex.domain.models.LegalType
import java.util.UUID

@Entity(
    tableName = "ideas",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IdeaEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "user_id")
    var userId: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "legal_type")
    var legalType: String = "",
    @ColumnInfo(name = "score")
    var score: Int = 0
) {
    constructor() : this("", "", "", "", "", 0)
    companion object {
        fun fromDomain(
            id: UUID,
            userId: UUID,
            title: String,
            description: String,
            legalType: LegalType,
            score: UInt
        ): IdeaEntity {
            return IdeaEntity(
                id.toString(),
                userId.toString(),
                title,
                description,
                legalType.name,
                score.toInt()
            )
        }
    }
}

