    fun addApiKey(providerName: String, key: String, baseUrl: String?) {
        viewModelScope.launch {
            val config = ApiKeyConfig(
                id = UUID.randomUUID().toString(),
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
