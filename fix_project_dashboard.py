import re

with open('app/src/main/java/com/example/presentation/screens/DashboardScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('ProjectMetadata', 'Project')
content = content.replace('project.lastModified', 'project.lastUpdated')
content = content.replace('project.description', 'project.status')

with open('app/src/main/java/com/example/presentation/screens/DashboardScreen.kt', 'w') as f:
    f.write(content)
