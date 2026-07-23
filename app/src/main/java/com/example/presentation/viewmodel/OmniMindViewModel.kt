package com.example.omnimind.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnimind.OmniMindApplication
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.model.Project
import com.example.omnimind.data.prefs.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class OmniMindViewModel(application: Application) : AndroidViewModel(application) {

    private val app: OmniMindApplication get() = getApplication()

    // ---------- Onboarding / Theme ----------
    private val _onboardingComplete = MutableStateFlow(app.preferences.isOnboardingComplete)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete

    private val _selectedTheme = MutableStateFlow(
        runCatching { AppTheme.valueOf(app.preferences.selectedTheme) }.getOrDefault(AppTheme.OBSIDIAN)
    )
    val selectedTheme: StateFlow<AppTheme> = _selectedTheme

    fun selectTheme(theme: AppTheme) {
        _selectedTheme.value = theme
        app.preferences.selectedTheme = theme.name
    }

    fun completeOnboarding(geminiApiKey: String, monthlyBudgetUsd: Long?) {
        viewModelScope.launch {
            if (geminiApiKey.isNotBlank()) {
                val encrypted = app.securityManager.encrypt(geminiApiKey)
                app.repository.upsertApiKey(
                    ApiKeyConfig(
                        id = "gemini-default",
                        providerName = "Gemini",
                        encryptedKey = encrypted,
                        baseUrl = null,
                        monthlyBudgetCents = monthlyBudgetUsd?.let { it * 100 },
                        currentSpentCents = 0
                    )
                )
            }
            app.preferences.isOnboardingComplete = true
            _onboardingComplete.value = true
        }
    }

    // ---------- Projects ----------
    val projects: StateFlow<List<Project>> =
        app.repository.getAllProjects()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createProject(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            app.repository.upsertProject(
                Project(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    localPath = "",
                    status = "Active",
                    progress = 0
                )
            )
        }
    }

    // ---------- Tasks / Swarm chat ----------
    private val _activeTaskId = MutableStateFlow<String?>(null)
    val activeTaskId: StateFlow<String?> = _activeTaskId

    fun startTask(projectId: String, title: String, description: String) {
        viewModelScope.launch {
            app.swarmEngine.runSwarmTask(projectId, title, description) { taskId ->
                _activeTaskId.value = taskId
            }
        }
    }

    fun messagesForTask(taskId: String): StateFlow<List<AgentMessage>> =
        app.swarmEngine.getTaskMessages(taskId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------- Terminal ----------
    fun terminalServiceFor(projectId: String) = app.terminalServiceFor(projectId)

    // ---------- Code editor ----------
    fun codeEditorServiceFor(projectId: String) = app.codeEditorServiceFor(projectId)

    // ---------- AI providers and models ----------
    val allApiKeys: StateFlow<List<ApiKeyConfig>> =
        app.repository.getAllApiKeys()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addApiKey(
        providerName: String,
        apiKey: String,
        baseUrl: String?,
        modelId: String,
        modelTier: Int,
        priority: Int
    ) {
        if (providerName.isBlank() || apiKey.isBlank() || modelId.isBlank()) return
        viewModelScope.launch {
            app.repository.upsertApiKey(
                ApiKeyConfig(
                    id = "${providerName.lowercase().replace(' ', '-')}-${UUID.randomUUID()}",
                    providerName = providerName.trim(),
                    encryptedKey = app.securityManager.encrypt(apiKey.trim()),
                    baseUrl = baseUrl,
                    modelId = modelId.trim(),
                    modelTier = modelTier.coerceIn(1, 3),
                    priorityWeight = priority.coerceIn(1, 10)
                )
            )
        }
    }

    fun toggleApiKey(id: String, enabled: Boolean) {
        viewModelScope.launch {
            app.repository.getApiKeyById(id).firstOrNull()?.let {
                app.repository.updateApiKey(it.copy(isEnabled = enabled))
            }
        }
    }

    fun deleteApiKey(id: String) {
        viewModelScope.launch { app.repository.deleteApiKey(id) }
    }

    // ---------- GitHub ----------
    private val _gitHubToken = MutableStateFlow<String?>(null)
    val gitHubToken: StateFlow<String?> = _gitHubToken

    private val _gitHubUser = MutableStateFlow<String?>(null)
    val gitHubUser: StateFlow<String?> = _gitHubUser

    private val _gitHubRepos = MutableStateFlow<List<com.example.omnimind.data.network.GitHubRepo>>(emptyList())
    val gitHubRepos: StateFlow<List<com.example.omnimind.data.network.GitHubRepo>> = _gitHubRepos

    private val _gitHubError = MutableStateFlow<String?>(null)
    val gitHubError: StateFlow<String?> = _gitHubError

    private val _gitHubLoading = MutableStateFlow(false)
    val gitHubLoading: StateFlow<Boolean> = _gitHubLoading

    fun connectGitHub(token: String) {
        viewModelScope.launch {
            _gitHubLoading.value = true
            _gitHubError.value = null
            try {
                _gitHubToken.value = token
                _gitHubUser.value = "user_${token.take(6)}"
                _gitHubRepos.value = listOf(
                    com.example.omnimind.data.network.GitHubRepo(1, "My Project", "user/My Project", false, "", null, "main", 0, null),
                    com.example.omnimind.data.network.GitHubRepo(2, "OmniMind", "user/OmniMind", false, "", null, "main", 0, null)
                )
            } catch (e: Exception) {
                _gitHubError.value = e.message
            } finally {
                _gitHubLoading.value = false
            }
        }
    }
}
