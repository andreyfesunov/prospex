package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.LegalTypeSurveyOptionEntity

@Dao
interface LegalTypeSurveyOptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LegalTypeSurveyOptionEntity)

    @Query("SELECT * FROM legal_type_survey_options WHERE question_id = :questionId ORDER BY id")
    suspend fun getByQuestionId(questionId: String): List<LegalTypeSurveyOptionEntity>
}
