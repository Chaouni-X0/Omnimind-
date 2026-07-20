import re

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'r') as f:
    content = f.read()

# Make sure to import SecurityManager
if 'import com.example.data.security.SecurityManager' not in content:
    content = content.replace('import com.example.data.repository.OmniMindRepository\n', 'import com.example.data.repository.OmniMindRepository\nimport com.example.data.security.SecurityManager\n')

# Add securityManager to ViewModel
if 'private val securityManager = SecurityManager()' not in content:
    content = content.replace('class OmniMindViewModel(private val repository: OmniMindRepository) : ViewModel() {\n', 'class OmniMindViewModel(private val repository: OmniMindRepository) : ViewModel() {\n\n    private val securityManager = SecurityManager()\n')

# Fix addApiKey
new_addApiKey = """
    fun addApiKey(providerName: String, key: String, baseUrl: String?, priority: Int, budget: Long) {
        viewModelScope.launch {
            val encrypted = securityManager.encrypt(key)
            val newKey = ApiKeyConfig(
                id = UUID.randomUUID().toString(),
                providerName = providerName,
                encryptedKey = encrypted,
                baseUrl = baseUrl,
                priorityWeight = priority,
                monthlyBudgetCents = if (budget > 0) budget else null
            )
            repository.insertApiKey(newKey)
        }
    }
    
    fun updateApiKey(config: ApiKeyConfig, rawKey: String?) {
        viewModelScope.launch {
            val updatedConfig = if (!rawKey.isNullOrBlank()) {
                config.copy(encryptedKey = securityManager.encrypt(rawKey))
            } else {
                config
            }
            repository.updateApiKey(updatedConfig)
        }
    }
    
    fun toggleApiKey(config: ApiKeyConfig, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.updateApiKey(config.copy(isEnabled = isEnabled))
        }
    }
    
    fun deleteApiKey(config: ApiKeyConfig) {
        viewModelScope.launch {
            repository.deleteApiKey(config)
        }
    }
"""

content = re.sub(r'fun addApiKey.*?launch \{.*?\n\s*\}\n\s*\}', new_addApiKey.strip(), content, flags=re.DOTALL)

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'w') as f:
    f.write(content)
