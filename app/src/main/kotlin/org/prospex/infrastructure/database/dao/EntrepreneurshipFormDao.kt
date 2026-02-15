package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.EntrepreneurshipFormEntity

@Dao
interface EntrepreneurshipFormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(form: EntrepreneurshipFormEntity)

    @Query("SELECT * FROM entrepreneurship_forms ORDER BY legal_type")
    suspend fun getAll(): List<EntrepreneurshipFormEntity>

    @Query("SELECT COUNT(*) FROM entrepreneurship_forms")
    suspend fun count(): Int
}
