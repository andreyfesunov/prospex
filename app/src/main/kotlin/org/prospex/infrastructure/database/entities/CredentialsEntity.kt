package org.prospex.infrastructure.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "credentials",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CredentialsEntity(
    @PrimaryKey
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "password_hash")
    var passwordHash: String = ""
) {
    constructor() : this("", "")
}

