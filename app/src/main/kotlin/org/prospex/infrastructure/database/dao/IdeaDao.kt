package org.prospex.infrastructure.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.prospex.infrastructure.database.entities.IdeaEntity

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(idea: IdeaEntity)
    
    @Update
    suspend fun update(idea: IdeaEntity)
    
    @Query("SELECT * FROM ideas WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): IdeaEntity?
    
    @Query("SELECT * FROM ideas WHERE user_id = :userId ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getByUserId(userId: String, limit: Int, offset: Int): List<IdeaEntity>
    
    @Query("SELECT COUNT(*) FROM ideas WHERE user_id = :userId")
    suspend fun countByUserId(userId: String): Int
    
    @androidx.room.Delete
    suspend fun delete(idea: IdeaEntity)
}

