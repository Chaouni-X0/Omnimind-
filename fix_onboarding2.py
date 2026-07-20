import re

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('modifier = Modifier.height(56.dp).fillMaxWidth(0.6f).bounceClick(),', 'modifier = Modifier.height(56.dp).fillMaxWidth(0.6f),')
content = content.replace('modifier = Modifier.fillMaxWidth().height(88.dp).bounceClick(),', 'modifier = Modifier.fillMaxWidth().height(88.dp),')

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'w') as f:
    f.write(content)
