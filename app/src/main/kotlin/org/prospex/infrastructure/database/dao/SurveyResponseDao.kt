package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.prospex.infrastructure.database.entities.SurveyResponseEntity

@Dao
interface SurveyResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(response: SurveyResponseEntity)
    
    @Update
    suspend fun update(response: SurveyResponseEntity)
    
    @Query("SELECT * FROM survey_responses WHERE idea_id = :ideaId LIMIT 1")
    suspend fun getByIdeaId(ideaId: String): SurveyResponseEntity?
}

