import re

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'r') as f:
    content = f.read()

# Make sure to import SecurityManager
if 'import com.example.data.security.SecurityManager' not in content:
    content = content.replace('import com.example.data.repository.OmniMindRepository\n', 'import com.example.data.repository.OmniMindRepository\nimport com.example.data.security.SecurityManager\n')

# Add securityManager to ViewModel
if 'private val securityManager = SecurityManager()' not in content:
    content = content.replace('class OmniMindViewModel(application: Application) : AndroidViewModel(application) {\n', 'class OmniMindViewModel(application: Application) : AndroidViewModel(application) {\n\n    private val securityManager = SecurityManager()\n')

# Also add val apiKeys: Flow<List<ApiKeyConfig>> = repository.allApiKeys
if 'val apiKeys: Flow<List<ApiKeyConfig>>' not in content:
    content = content.replace('private val sharedPrefs = application.getSharedPreferences("omnimind_prefs", Context.MODE_PRIVATE)\n', 'private val sharedPrefs = application.getSharedPreferences("omnimind_prefs", Context.MODE_PRIVATE)\n\n    val apiKeys = repository.allApiKeys\n')

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'w') as f:
    f.write(content)
