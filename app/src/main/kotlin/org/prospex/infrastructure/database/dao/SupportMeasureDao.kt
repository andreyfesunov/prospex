package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.prospex.infrastructure.database.entities.SupportMeasureEntity

@Dao
interface SupportMeasureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(measure: SupportMeasureEntity)
}

