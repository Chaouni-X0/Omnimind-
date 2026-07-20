import sys

content = open('./app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt').read()

# Let's find "fun createProject(name: String, path: String)"
idx = content.find("fun createProject(name: String, path: String)")

if idx != -1:
    content = content[:idx]

new_part = """
    fun createProject(name: String, path: String) {
        viewModelScope.launch {
            val id = java.util.UUID.randomUUID().toString()
            val project = Project(
                id = id,
                name = name,
                localPath = path,
                status = "Quiet",
                progress = 0,
                filesJson = \"\"\"["README.md", "build.gradle.kts", "MainActivity.kt"]\"\"\"
            )
            repository.insertProject(project)
            _selectedProjectId.value = id
            sharedPrefs.edit().putString("selected_project_id", id).apply()
        }
    }

    fun createProjectFromPrompt(prompt: String): String {
        val id = java.util.UUID.randomUUID().toString()
        val name = if (prompt.length > 20) prompt.take(20) + "..." else prompt
        viewModelScope.launch {
            val project = Project(
                id = id,
                name = name,
                localPath = "/",
                status = "Active",
                progress = 0,
                filesJson = \"\"\"["README.md"]\"\"\"
            )
            repository.insertProject(project)
            _selectedProjectId.value = id
            sharedPrefs.edit().putString("selected_project_id", id).apply()
            
            val taskId = java.util.UUID.randomUUID().toString()
            val task = AgentTask(
                id = taskId,
                projectId = id,
                title = name,
                prompt = prompt,
                status = "In Progress"
            )
            repository.insertTask(task)
            _activeTaskId.value = taskId

            val msg = AgentMessage(
                id = java.util.UUID.randomUUID().toString(),
                taskId = taskId,
                agentId = "User",
                agentName = "User",
                messageText = prompt,
                verdictType = "None"
            )
            repository.insertMessage(msg)
            
            launch {
                swarmEngine.runSwarmTask(taskId)
            }
        }
        return id
    }

    fun addApiKey(providerName: String, key: String, baseUrl: String?) {
        viewModelScope.launch {
            val config = ApiKeyConfig(
                id = java.util.UUID.randomUUID().toString(),
                providerName = providerName,
                encryptedKey = securityManager.encrypt(key),
                baseUrl = baseUrl,
                priorityWeight = 5,
                monthlyBudgetCents = null,
                isEnabled = true,
                currentSpentCents = 0L,
                lastError = null,
                lastUsedTimestamp = 0L
            )
            repository.insertApiKey(config)
        }
    }

    fun toggleApiKey(id: String, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateApiKeyEnabled(id, isEnabled)
        }
    }

    fun deleteApiKey(id: String) {
        viewModelScope.launch {
            repository.deleteApiKeyById(id)
        }
    }

    // Task Operations
    fun requestEstimate(prompt: String) {
        viewModelScope.launch {
            val currentProj = selectedProject.value ?: return@launch
            // Estimate size
            val projectSize = 5000 // demo size
            val filesCount = 3
            val complexity = if (prompt.length > 50) "hard" else if (prompt.length > 20) "medium" else "easy"
            val est = apiPoolManager.estimateTask(projectSize, filesCount, complexity)
            // _currentEstimate.value = est
        }
    }

    fun clearEstimate() {
        // _currentEstimate.value = null
    }

    fun submitTask(prompt: String, approvedEstimate: TaskEstimate) {
        viewModelScope.launch {
            val projectId = _selectedProjectId.value ?: return@launch
            val taskId = java.util.UUID.randomUUID().toString()
            val newTask = AgentTask(
                id = taskId,
                projectId = projectId,
                title = if (prompt.length > 30) prompt.take(30) + "..." else prompt,
                prompt = prompt,
                status = "Pending",
                progress = 0,
                costEstimateCents = approvedEstimate.estimatedCostCents,
                timeEstimateSeconds = approvedEstimate.estimatedDurationSeconds,
                isApprovedByEstimator = true
            )
            repository.insertTask(newTask)
            _activeTaskId.value = taskId
            // _currentEstimate.value = null
            
            val proj = repository.getProjectById(projectId)
            if (proj != null) {
                repository.updateProject(proj.copy(status = "Active"))
            }
            swarmEngine.runSwarmTask(taskId)
        }
    }

    fun applySandboxToProject(sandboxRunId: String) {
        viewModelScope.launch {
            val activeId = _activeTaskId.value ?: return@launch
            val run = repository.getSandboxRunById(sandboxRunId) ?: return@launch
            repository.updateSandboxRun(run.copy(isApplied = true))
            val task = repository.getTaskById(activeId)
            if (task != null) {
                repository.updateTask(task.copy(status = "Completed", progress = 100))
            }
            val projectId = _selectedProjectId.value
            if (projectId != null) {
                val proj = repository.getProjectById(projectId)
                if (proj != null) {
                    repository.updateProject(proj.copy(status = "Quiet", progress = 100))
                }
            }
        }
    }

    fun rejectSandboxChanges(sandboxRunId: String) {
        viewModelScope.launch {
            val activeId = _activeTaskId.value ?: return@launch
            val run = repository.getSandboxRunById(sandboxRunId) ?: return@launch
            repository.updateSandboxRun(run.copy(isIgnored = true))
            val task = repository.getTaskById(activeId)
            if (task != null) {
                repository.updateTask(task.copy(status = "Vetoed", progress = 100))
            }
            val projectId = _selectedProjectId.value
            if (projectId != null) {
                val proj = repository.getProjectById(projectId)
                if (proj != null) {
                    repository.updateProject(proj.copy(status = "Quiet"))
                }
            }
        }
    }

    fun insertMessage(message: AgentMessage) {
        viewModelScope.launch {
            repository.insertMessage(message)
        }
    }
}
"""

open('./app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'w').write(content + new_part)
