    fun addApiKey(providerName: String, key: String, baseUrl: String?, priorityWeight: Int = 5, monthlyBudgetCents: Long? = null) {
        viewModelScope.launch {
            val config = ApiKeyConfig(
                id = java.util.UUID.randomUUID().toString(),
                providerName = providerName,
                encryptedKey = securityManager.encrypt(key),
                baseUrl = baseUrl,
                priorityWeight = priorityWeight,
                monthlyBudgetCents = monthlyBudgetCents,
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
            val key = repository.getApiKeyById(id) ?: return@launch
            repository.updateApiKey(key.copy(isEnabled = isEnabled))
        }
    }

    fun deleteApiKey(id: String) {
        viewModelScope.launch {
            val key = repository.getApiKeyById(id) ?: return@launch
            repository.deleteApiKey(key)
        }
    }
