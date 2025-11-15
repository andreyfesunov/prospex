package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.SupportMeasureEntity

@Dao
interface SupportMeasureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measure: SupportMeasureEntity)
    
    @Query("SELECT * FROM support_measures ORDER BY measure_type, title")
    suspend fun getAll(): List<SupportMeasureEntity>
    
    @Query("SELECT COUNT(*) FROM support_measures")
    suspend fun count(): Int
}

