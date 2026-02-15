package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.prospex.domain.models.EntrepreneurshipForm
import org.prospex.domain.models.LegalType
import java.util.UUID

@Entity(tableName = "entrepreneurship_forms")
data class EntrepreneurshipFormEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "legal_type")
    var legalType: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "features")
    var features: String = "",
    @ColumnInfo(name = "requirements")
    var requirements: String = ""
) {
    constructor() : this("", "", "", "", "", "")
    companion object {
        fun fromDomain(form: EntrepreneurshipForm): EntrepreneurshipFormEntity {
            return EntrepreneurshipFormEntity(
                form.id.toString(),
                form.title,
                form.legalType.name,
                form.description,
                form.features,
                form.requirements
            )
        }

        fun toDomain(entity: EntrepreneurshipFormEntity): EntrepreneurshipForm {
            return EntrepreneurshipForm(
                id = UUID.fromString(entity.id),
                title = entity.title,
                legalType = LegalType.valueOf(entity.legalType),
                description = entity.description,
                features = entity.features,
                requirements = entity.requirements
            )
        }
    }
}
