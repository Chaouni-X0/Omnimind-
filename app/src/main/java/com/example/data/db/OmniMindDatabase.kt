package com.example.omnimind.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.AgentTask
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
    version = 3,
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
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE api_keys ADD COLUMN modelId TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE api_keys ADD COLUMN modelTier INTEGER NOT NULL DEFAULT 1")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                runCatching {
                    database.execSQL("ALTER TABLE agent_tasks ADD COLUMN costEstimateCents INTEGER NOT NULL DEFAULT 0")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_tasks ADD COLUMN timeEstimateSeconds INTEGER NOT NULL DEFAULT 0")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_tasks ADD COLUMN isApprovedByEstimator INTEGER NOT NULL DEFAULT 0")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_tasks ADD COLUMN filesModifiedCount INTEGER NOT NULL DEFAULT 0")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_messages ADD COLUMN thinking TEXT")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_messages ADD COLUMN actions TEXT")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_messages ADD COLUMN output TEXT")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_messages ADD COLUMN verdictType TEXT NOT NULL DEFAULT 'NONE'")
                }
                runCatching {
                    database.execSQL("ALTER TABLE agent_messages ADD COLUMN verdictReason TEXT")
                }
            }
        }
    }
}
