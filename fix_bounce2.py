import re

with open('app/src/main/java/com/example/presentation/screens/apicenter/ApiCenterScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('modifier = Modifier.bounceClick()', 'modifier = Modifier.bounceClick { showAddDialog = true }')

with open('app/src/main/java/com/example/presentation/screens/apicenter/ApiCenterScreen.kt', 'w') as f:
    f.write(content)
