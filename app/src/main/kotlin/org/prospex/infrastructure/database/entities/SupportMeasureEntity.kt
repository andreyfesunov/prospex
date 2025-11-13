package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "support_measures")
data class SupportMeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "min_score")
    var minScore: Int = 0
) {
    constructor() : this("", "", 0)
    companion object {
        fun fromDomain(id: UUID, title: String, minScore: UInt): SupportMeasureEntity {
            return SupportMeasureEntity(
                id.toString(),
                title,
                minScore.toInt()
            )
        }
    }
}

