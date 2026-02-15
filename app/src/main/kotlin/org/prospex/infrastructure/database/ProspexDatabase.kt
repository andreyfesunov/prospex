package org.prospex.infrastructure.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.prospex.infrastructure.database.dao.*
import org.prospex.infrastructure.database.entities.*

@Database(
    entities = [
        UserEntity::class,
        CredentialsEntity::class,
        IdeaEntity::class,
        QuestionEntity::class,
        QuestionOptionEntity::class,
        SurveyResponseEntity::class,
        SupportMeasureEntity::class,
        EntrepreneurshipFormEntity::class,
        LegalTypeSurveyQuestionEntity::class,
        LegalTypeSurveyOptionEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class ProspexDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun credentialsDao(): CredentialsDao
    abstract fun ideaDao(): IdeaDao
    abstract fun questionDao(): QuestionDao
    abstract fun questionOptionDao(): QuestionOptionDao
    abstract fun surveyResponseDao(): SurveyResponseDao
    abstract fun supportMeasureDao(): SupportMeasureDao
    abstract fun entrepreneurshipFormDao(): EntrepreneurshipFormDao
    abstract fun legalTypeSurveyQuestionDao(): LegalTypeSurveyQuestionDao
    abstract fun legalTypeSurveyOptionDao(): LegalTypeSurveyOptionDao
    
    companion object {
        @Volatile
        private var INSTANCE: ProspexDatabase? = null
        
        fun getDatabase(context: Context): ProspexDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProspexDatabase::class.java,
                    "business_ideas.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

