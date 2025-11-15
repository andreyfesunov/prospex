package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.prospex.domain.models.LegalType
import org.prospex.domain.models.MeasureType
import org.prospex.domain.models.SupportMeasure
import java.util.UUID

@Entity(tableName = "support_measures")
data class SupportMeasureEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "measure_type")
    var measureType: String = "",
    @ColumnInfo(name = "legal_types")
    var legalTypes: String = "",
    @ColumnInfo(name = "amount")
    var amount: String = "",
    @ColumnInfo(name = "features")
    var features: String = "",
    @ColumnInfo(name = "covers")
    var covers: String = "",
    @ColumnInfo(name = "where_to_apply")
    var whereToApply: String = ""
) {
    constructor() : this("", "", "", "", "", "", "", "")
    companion object {
        fun fromDomain(measure: SupportMeasure): SupportMeasureEntity {
            return SupportMeasureEntity(
                measure.id.toString(),
                measure.title,
                measure.measureType.name,
                measure.legalTypes.joinToString(",") { it.name },
                measure.amount,
                measure.features,
                measure.covers,
                measure.whereToApply
            )
        }
        
        fun toDomain(entity: SupportMeasureEntity): SupportMeasure {
            return SupportMeasure(
                id = UUID.fromString(entity.id),
                title = entity.title,
                measureType = MeasureType.valueOf(entity.measureType),
                legalTypes = entity.legalTypes.split(",").map { LegalType.valueOf(it) }.toTypedArray(),
                amount = entity.amount,
                features = entity.features,
                covers = entity.covers,
                whereToApply = entity.whereToApply
            )
        }
    }
}

