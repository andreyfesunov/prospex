package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "email")
    var email: String = ""
) {
    constructor() : this("", "")
    companion object {
        fun fromDomain(id: UUID, email: String): UserEntity {
            return UserEntity(id.toString(), email)
        }
    }
}

