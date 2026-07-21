package com.example.omnimind.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.omnimind.OmniMindApplication
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.data.model.Project
import com.example.omnimind.data.network.GitHubContentItem
import com.example.omnimind.data.network.GitHubRepo
import com.example.omnimind.data.prefs.AppTheme
import com.example.omnimind.domain.editor.CodeEditorService
import com.example.omnimind.domain.terminal.TerminalService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class OmniMindViewModel(application: Application) : AndroidViewModel(application) {

    private val app: OmniMindApplication
        get() = getApplication()

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
    fun terminalServiceFor(projectId: String): TerminalService = app.terminalServiceFor(projectId)

    // ---------- Code editor ----------
    fun codeEditorServiceFor(projectId: String): CodeEditorService = app.codeEditorServiceFor(projectId)

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

    fun updateApiKey(apiKey: ApiKeyConfig, newPlaintextKey: String?) {
        viewModelScope.launch {
            val encryptedKey = newPlaintextKey?.takeIf { it.isNotBlank() }
                ?.let(app.securityManager::encrypt) ?: apiKey.encryptedKey
            app.repository.updateApiKey(apiKey.copy(encryptedKey = encryptedKey))
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
    private val _gitHubToken = MutableStateFlow("")
    val gitHubToken: StateFlow<String> = _gitHubToken

    private val _gitHubRepos = MutableStateFlow<List<GitHubRepo>>(emptyList())
    val gitHubRepos: StateFlow<List<GitHubRepo>> = _gitHubRepos

    private val _gitHubError = MutableStateFlow<String?>(null)
    val gitHubError: StateFlow<String?> = _gitHubError

    private val _gitHubLoading = MutableStateFlow(false)
    val gitHubLoading: StateFlow<Boolean> = _gitHubLoading

    private val _gitHubContents = MutableStateFlow<List<GitHubContentItem>>(emptyList())
    val gitHubContents: StateFlow<List<GitHubContentItem>> = _gitHubContents

    private val _gitHubUser = MutableStateFlow<String?>(null)
    val gitHubUser: StateFlow<String?> = _gitHubUser

    private val _gitHubCurrentRepo = MutableStateFlow<Pair<String, String>?>(null)
    val gitHubCurrentRepo: StateFlow<Pair<String, String>?> = _gitHubCurrentRepo

    private val _gitHubCurrentPath = MutableStateFlow("")
    val gitHubCurrentPath: StateFlow<String> = _gitHubCurrentPath

    private val _gitHubFileContent = MutableStateFlow<Pair<String, String>?>(null)
    val gitHubFileContent: StateFlow<Pair<String, String>?> = _gitHubFileContent

    private val _gitHubMessage = MutableStateFlow<String?>(null)
    val gitHubMessage: StateFlow<String?> = _gitHubMessage

    fun connectGitHub(token: String) {
        _gitHubToken.value = token
        viewModelScope.launch {
            app.repository.upsertApiKey(
                ApiKeyConfig(
                    id = "github-default",
                    providerName = "GitHub",
                    encryptedKey = app.securityManager.encrypt(token),
                    baseUrl = null
                )
            )
            app.gitHubService.getUser(token)
                .onSuccess { _gitHubUser.value = it.login }
                .onFailure { _gitHubUser.value = null }
            loadGitHubRepos()
        }
    }

    fun loadGitHubRepos() {
        val token = _gitHubToken.value
        if (token.isBlank()) return
        viewModelScope.launch {
            _gitHubLoading.value = true
            _gitHubError.value = null
            _gitHubCurrentRepo.value = null
            _gitHubCurrentPath.value = ""
            _gitHubContents.value = emptyList()
            app.gitHubService.listMyRepositories(token)
                .onSuccess { _gitHubRepos.value = it }
                .onFailure { _gitHubError.value = it.message ?: "تعذر تحميل المستودعات" }
            _gitHubLoading.value = false
        }
    }

    fun loadGitHubContents(owner: String, repo: String, path: String = "") {
        val token = _gitHubToken.value
        if (token.isBlank()) return
        viewModelScope.launch {
            _gitHubLoading.value = true
            _gitHubError.value = null
            _gitHubCurrentRepo.value = owner to repo
            _gitHubCurrentPath.value = path
            app.gitHubService.listContents(owner, repo, path, token)
                .onSuccess { items ->
                    _gitHubContents.value = items.sortedWith(
                        compareByDescending<GitHubContentItem> { it.type == "dir" }.thenBy { it.name }
                    )
                }
                .onFailure { _gitHubError.value = it.message ?: "تعذر تحميل الملفات" }
            _gitHubLoading.value = false
        }
    }

    fun openGitHubFile(item: GitHubContentItem) {
        val url = item.download_url ?: return
        viewModelScope.launch {
            _gitHubLoading.value = true
            app.gitHubService.downloadFileContent(url, _gitHubToken.value)
                .onSuccess { _gitHubFileContent.value = item.name to it }
                .onFailure { _gitHubError.value = it.message ?: "تعذر فتح الملف" }
            _gitHubLoading.value = false
        }
    }

    fun clearGitHubFile() { _gitHubFileContent.value = null }
    fun clearGitHubMessage() { _gitHubMessage.value = null }

    fun importGitHubFileToProject(projectId: String, item: GitHubContentItem) {
        val url = item.download_url ?: return
        viewModelScope.launch {
            app.gitHubService.downloadFileContent(url, _gitHubToken.value)
                .onSuccess {
                    app.codeEditorServiceFor(projectId).writeFile(item.path, it)
                    _gitHubMessage.value = "تم استيراد ${item.name} إلى المشروع"
                }
                .onFailure { _gitHubError.value = it.message ?: "فشل الاستيراد" }
        }
    }

    fun cloneRepoToProject(projectId: String, owner: String, repo: String) {
        val token = _gitHubToken.value
        if (token.isBlank()) return
        viewModelScope.launch {
            _gitHubLoading.value = true
            var count = 0
            suspend fun walk(path: String) {
                app.gitHubService.listContents(owner, repo, path, token).onSuccess { items ->
                    items.forEach { item ->
                        if (item.type == "dir") walk(item.path)
                        else item.download_url?.let { url ->
                            app.gitHubService.downloadFileContent(url, token).onSuccess { content ->
                                if (app.codeEditorServiceFor(projectId).writeFile(item.path, content)) count++
                            }
                        }
                    }
                }.onFailure { _gitHubError.value = it.message }
            }
            walk("")
            _gitHubMessage.value = "تم استنساخ $repo ($count ملف) إلى المشروع"
            _gitHubLoading.value = false
        }
    }
}
