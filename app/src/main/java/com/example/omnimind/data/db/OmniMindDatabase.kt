package com.example.omnimind.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.omnimind.data.model.AgentTask
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.model.Project
import com.example.omnimind.data.model.SandboxRun

@Database(
    entities = [
        AgentTask::class,
        AgentMessage::class,
        ApiKeyConfig::class,
        Project::class,
        SandboxRun::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OmniMindDatabase : RoomDatabase() {
    abstract fun omniMindDao(): OmniMindDao
    
    companion object {
        @Volatile
        private var INSTANCE: OmniMindDatabase? = null
        
        fun getDatabase(context: android.content.Context): OmniMindDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    OmniMindDatabase::class.java,
                    "omni_mind_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
