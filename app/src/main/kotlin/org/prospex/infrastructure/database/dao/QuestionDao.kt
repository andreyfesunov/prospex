package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: QuestionEntity)
    
    @Query("SELECT * FROM questions WHERE legal_type = :legalType")
    suspend fun getByLegalType(legalType: String): List<QuestionEntity>
    
    @Query("SELECT DISTINCT q.* FROM questions q INNER JOIN question_options qo ON q.id = qo.questionId WHERE qo.id IN (:optionIds)")
    suspend fun getByOptionIds(optionIds: List<String>): List<QuestionEntity>
}

