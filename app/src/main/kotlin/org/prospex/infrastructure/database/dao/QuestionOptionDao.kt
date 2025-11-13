package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.prospex.infrastructure.database.entities.QuestionOptionEntity

@Dao
interface QuestionOptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(option: QuestionOptionEntity)
    
    @Query("SELECT * FROM question_options WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<QuestionOptionEntity>
    
    @Query("SELECT * FROM question_options WHERE questionId = :questionId")
    suspend fun getByQuestionId(questionId: String): List<QuestionOptionEntity>
    
    @Query("SELECT COUNT(*) FROM question_options")
    suspend fun count(): Int
}

