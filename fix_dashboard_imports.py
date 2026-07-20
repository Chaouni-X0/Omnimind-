import re

with open('app/src/main/java/com/example/presentation/screens/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('import com.example.data.model.ProjectMetadata\n', '')
content = content.replace('ProjectMetadata', 'com.example.data.project.ProjectMetadata')

with open('app/src/main/java/com/example/presentation/screens/DashboardScreen.kt', 'w') as f:
    f.write(content)
