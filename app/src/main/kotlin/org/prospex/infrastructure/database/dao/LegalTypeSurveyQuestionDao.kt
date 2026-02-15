package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.LegalTypeSurveyQuestionEntity

@Dao
interface LegalTypeSurveyQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LegalTypeSurveyQuestionEntity)

    @Query("SELECT * FROM legal_type_survey_questions ORDER BY order_index ASC")
    suspend fun getAll(): List<LegalTypeSurveyQuestionEntity>
}
