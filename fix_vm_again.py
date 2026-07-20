import re

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'r') as f:
    content = f.read()

# remove my duplicated apiKeys
content = re.sub(r'val apiKeys = repository\.allApiKeys', '', content)

# update toggleApiKey and deleteApiKey to accept String id instead of config OR update ApiCenterScreen to pass config.
# Wait, ApiCenterScreen has `config = keyConfig`, so it can pass `keyConfig`!

with open('app/src/main/java/com/example/presentation/viewmodel/OmniMindViewModel.kt', 'w') as f:
    f.write(content)
