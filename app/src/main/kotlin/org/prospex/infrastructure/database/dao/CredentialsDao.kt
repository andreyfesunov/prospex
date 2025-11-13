package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.CredentialsEntity

@Dao
interface CredentialsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(credentials: CredentialsEntity)
    
    @Query("SELECT * FROM credentials WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): CredentialsEntity?
}

