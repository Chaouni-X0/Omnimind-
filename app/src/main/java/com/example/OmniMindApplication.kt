package com.example.omnimind

import android.app.Application
import com.example.omnimind.data.db.OmniMindDatabase
import com.example.omnimind.data.network.NetworkModule
import com.example.omnimind.data.prefs.AppPreferences
import com.example.omnimind.data.repository.OmniMindRepository
import com.example.omnimind.data.security.SecurityManager
import com.example.omnimind.domain.editor.CodeEditorService
import com.example.omnimind.domain.github.GitHubService
import com.example.omnimind.domain.llm.LlmRouter
import com.example.omnimind.domain.swarm.SwarmEngine
import com.example.omnimind.domain.terminal.TerminalService
import java.io.File

class OmniMindApplication : Application() {

    lateinit var repository: OmniMindRepository
        private set

    lateinit var preferences: AppPreferences
        private set

    lateinit var securityManager: SecurityManager
        private set

    lateinit var swarmEngine: SwarmEngine
        private set

    lateinit var gitHubService: GitHubService
        private set

    override fun onCreate() {
        super.onCreate()
        val database = OmniMindDatabase.getDatabase(this)
        repository = OmniMindRepository(database.omniMindDao())
        preferences = AppPreferences(this)
        securityManager = SecurityManager()
        val llmRouter = LlmRouter(
            dao = database.omniMindDao(),
            securityManager = securityManager,
            geminiApi = NetworkModule.geminiApiService,
            genericApi = NetworkModule.genericLlmApiService
        )
        swarmEngine = SwarmEngine(database.omniMindDao(), llmRouter)
        gitHubService = GitHubService(NetworkModule.gitHubApiService)
    }

    fun workspaceDirFor(projectId: String): File =
        File(filesDir, "projects/$projectId/workspace").apply { mkdirs() }

    fun terminalServiceFor(projectId: String): TerminalService =
        TerminalService(workspaceDirFor(projectId))

    fun codeEditorServiceFor(projectId: String): CodeEditorService =
        CodeEditorService(workspaceDirFor(projectId))
}
