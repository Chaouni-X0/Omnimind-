import re

with open('app/src/main/java/com/example/presentation/screens/apicenter/ApiCenterScreen.kt', 'r') as f:
    content = f.read()

# Replace "API Center" with "Connectors Center"
content = content.replace('"API Center"', '"Connectors Center"')
content = content.replace('Text("Provider (e.g., Gemini, OpenAI)")', 'Text("Provider (e.g., Gemini, OpenAI, GitHub, Jira)")')
content = content.replace('Text("Add API Key")', 'Text("Add Connector / API Key")')

with open('app/src/main/java/com/example/presentation/screens/apicenter/ApiCenterScreen.kt', 'w') as f:
    f.write(content)
