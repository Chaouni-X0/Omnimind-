import re

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('onClick = { viewModel.setTheme(theme.name) },\n                    modifier = Modifier.fillMaxWidth().height(88.dp).bounceClick(),', 'onClick = { viewModel.setTheme(theme.name) },\n                    modifier = Modifier.fillMaxWidth().height(88.dp),')

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'w') as f:
    f.write(content)
