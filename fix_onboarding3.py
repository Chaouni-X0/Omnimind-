import re

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'r') as f:
    content = f.read()

content = content.replace('import androidx.compose.material3.*', 'import androidx.compose.material3.*\nimport com.example.presentation.components.PremiumButton')
content = content.replace('Button(\n            onClick = onNext,', 'PremiumButton(\n            onClick = onNext,')
content = content.replace('Button(\n            onClick = {\n                if (apiKey.isNotBlank())', 'PremiumButton(\n            onClick = {\n                if (apiKey.isNotBlank())')

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'w') as f:
    f.write(content)
