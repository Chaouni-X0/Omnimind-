    fun createProjectFromPrompt(prompt: String): String {
        val id = UUID.randomUUID().toString()
        val name = if (prompt.length > 20) prompt.take(20) + "..." else prompt
        viewModelScope.launch {
            val project = Project(
                id = id,
                name = name,
                localPath = "/",
                status = "Active",
                progress = 0,
                filesJson = """["README.md"]"""
            )
            repository.insertProject(project)
            _selectedProjectId.value = id
            sharedPrefs.edit().putString("selected_project_id", id).apply()
            
            val taskId = UUID.randomUUID().toString()
            val task = AgentTask(
                id = taskId,
                projectId = id,
                title = name,
                originalUserGoal = prompt,
                status = "In Progress",
                requestingAgentId = "User"
            )
            repository.insertTask(task)
            _activeTaskId.value = taskId

            val msg = AgentMessage(
                id = UUID.randomUUID().toString(),
                taskId = taskId,
                agentId = "User",
                agentName = "User",
                messageText = prompt,
                verdictType = "None"
            )
            repository.insertMessage(msg)
            
            swarmEngine.submitTask(task)
        }
        return id
    }
