package com.example.omnimind.data.repository

import com.example.omnimind.data.db.OmniMindDao
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.AgentTask
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.model.Project
import com.example.omnimind.data.model.SandboxRun
import kotlinx.coroutines.flow.Flow

/**
 * طبقة وسيطة بين الواجهة (ViewModel) وقاعدة البيانات (Room).
 * تبقي الـ ViewModel بعيدًا عن تفاصيل DAO المباشرة.
 */
class OmniMindRepository(private val dao: OmniMindDao) {

    // Projects
    fun getAllProjects(): Flow<List<Project>> = dao.getAllProjects()
    fun getProjectById(projectId: String): Flow<Project?> = dao.getProjectById(projectId)
    suspend fun upsertProject(project: Project) = dao.insertProject(project)
    suspend fun updateProject(project: Project) = dao.updateProject(project)

    // Tasks
    fun getTasksByProject(projectId: String): Flow<List<AgentTask>> = dao.getTasksByProject(projectId)
    fun getTaskById(taskId: String): Flow<AgentTask?> = dao.getTaskById(taskId)
    suspend fun insertTask(task: AgentTask) = dao.insertTask(task)
    suspend fun updateTask(task: AgentTask) = dao.updateTask(task)

    // Messages
    fun getMessagesByTask(taskId: String): Flow<List<AgentMessage>> = dao.getMessagesByTask(taskId)
    suspend fun insertMessage(message: AgentMessage) = dao.insertMessage(message)

    // API keys
    fun getEnabledApiKeys(): Flow<List<ApiKeyConfig>> = dao.getEnabledApiKeys()
    fun getAllApiKeys(): Flow<List<ApiKeyConfig>> = dao.getAllApiKeys()
    fun getApiKeyById(id: String): Flow<ApiKeyConfig?> = dao.getApiKeyById(id)
    suspend fun upsertApiKey(apiKey: ApiKeyConfig) = dao.insertApiKey(apiKey)
    suspend fun updateApiKey(apiKey: ApiKeyConfig) = dao.updateApiKey(apiKey)
    suspend fun deleteApiKey(id: String) = dao.deleteApiKey(id)

    // Sandbox runs
    fun getSandboxRunsByTask(taskId: String): Flow<List<SandboxRun>> = dao.getSandboxRunsByTask(taskId)
    suspend fun insertSandboxRun(run: SandboxRun) = dao.insertSandboxRun(run)
}
