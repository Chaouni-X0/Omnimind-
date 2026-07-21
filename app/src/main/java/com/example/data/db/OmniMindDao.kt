package com.example.omnimind.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.AgentTask
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.model.Project
import com.example.omnimind.data.model.SandboxRun
import kotlinx.coroutines.flow.Flow

@Dao
interface OmniMindDao {

    // ---------- AgentTask ----------
    @Insert
    suspend fun insertTask(task: AgentTask): Long

    @Update
    suspend fun updateTask(task: AgentTask)

    @Query("SELECT * FROM agent_tasks WHERE id = :taskId")
    fun getTaskById(taskId: String): Flow<AgentTask?>

    @Query("SELECT * FROM agent_tasks WHERE projectId = :projectId ORDER BY lastUpdated DESC")
    fun getTasksByProject(projectId: String): Flow<List<AgentTask>>

    // ---------- AgentMessage ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AgentMessage): Long

    @Query("SELECT * FROM agent_messages WHERE taskId = :taskId ORDER BY timestamp ASC")
    fun getMessagesByTask(taskId: String): Flow<List<AgentMessage>>

    // ---------- Project ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Update
    suspend fun updateProject(project: Project)

    @Query("SELECT * FROM projects ORDER BY lastUpdated DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getProjectById(projectId: String): Flow<Project?>

    // ---------- ApiKeyConfig ----------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiKey(apiKey: ApiKeyConfig): Long

    @Update
    suspend fun updateApiKey(apiKey: ApiKeyConfig)

    @Query("SELECT * FROM api_keys WHERE id = :id")
    fun getApiKeyById(id: String): Flow<ApiKeyConfig?>

    @Query("SELECT * FROM api_keys WHERE isEnabled = 1 ORDER BY priorityWeight DESC")
    fun getEnabledApiKeys(): Flow<List<ApiKeyConfig>>

    @Query("SELECT * FROM api_keys ORDER BY modelTier ASC, priorityWeight DESC")
    fun getAllApiKeys(): Flow<List<ApiKeyConfig>>

    @Query("DELETE FROM api_keys WHERE id = :id")
    suspend fun deleteApiKey(id: String)

    // ---------- SandboxRun ----------
    @Insert
    suspend fun insertSandboxRun(run: SandboxRun): Long

    @Query("SELECT * FROM sandbox_runs WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getSandboxRunsByTask(taskId: String): Flow<List<SandboxRun>>
}
