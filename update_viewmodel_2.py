import re

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'r') as f:
    content = f.read()

if 'val apiKeys: Flow<List<ApiKeyConfig>> = repository.allApiKeys' not in content:
    content = content.replace('class OmniMindViewModel(private val repository: OmniMindRepository) : ViewModel() {\n', 'class OmniMindViewModel(private val repository: OmniMindRepository) : ViewModel() {\n\n    val apiKeys: Flow<List<ApiKeyConfig>> = repository.allApiKeys\n')

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'w') as f:
    f.write(content)
