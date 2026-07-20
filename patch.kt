// Patch to add to OmniMindViewModel.kt
    fun createProjectWithId(id: String, name: String, path: String) {
        viewModelScope.launch {
            val project = Project(
                id = id,
                name = name,
                path = path,
                status = "Idle",
                lastUpdated = System.currentTimeMillis()
            )
            _projects.update { it + project }
            selectProject(id)
            
            // Add initial message
            val msg = com.example.data.model.AgentMessage(
                id = java.util.UUID.randomUUID().toString(),
                taskId = id,
                agentId = "User",
                agentName = "User",
                messageText = path,
                timestamp = System.currentTimeMillis()
            )
            _activeMessages.update { it + msg }
        }
    }
