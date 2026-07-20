import re

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'r') as f:
    content = f.read()

# Add bounceClick to buttons
content = content.replace('Button(\n            onClick = onNext,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f),\n            shape = MaterialTheme.shapes.extraLarge,\n            contentPadding = PaddingValues(horizontal = 32.dp)', 'Button(\n            onClick = onNext,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f).bounceClick(),\n            shape = MaterialTheme.shapes.extraLarge,\n            contentPadding = PaddingValues(horizontal = 32.dp)')

content = content.replace('Button(\n            onClick = onNext,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f),\n            shape = MaterialTheme.shapes.extraLarge\n        )', 'Button(\n            onClick = onNext,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f).bounceClick(),\n            shape = MaterialTheme.shapes.extraLarge\n        )')

content = content.replace('Button(\n            onClick = {\n                if (apiKey.isNotBlank()) {\n                    isVerifying = true\n                    // Simulate verification\n                    viewModel.addApiKey("Gemini", apiKey, null, 10, budget.toLongOrNull() ?: 0)\n                    onNext()\n                }\n            },\n            enabled = apiKey.isNotBlank() && !isVerifying,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f),\n            shape = MaterialTheme.shapes.extraLarge\n        )', 'Button(\n            onClick = {\n                if (apiKey.isNotBlank()) {\n                    isVerifying = true\n                    // Simulate verification\n                    viewModel.addApiKey("Gemini", apiKey, null, 10, budget.toLongOrNull() ?: 0)\n                    onNext()\n                }\n            },\n            enabled = apiKey.isNotBlank() && !isVerifying,\n            modifier = Modifier.height(56.dp).fillMaxWidth(0.6f).bounceClick(),\n            shape = MaterialTheme.shapes.extraLarge\n        )')

content = content.replace('import com.example.presentation.components.PremiumBackground\n', 'import com.example.presentation.components.PremiumBackground\nimport com.example.presentation.components.bounceClick\n')

with open('app/src/main/java/com/example/presentation/screens/OnboardingScreen.kt', 'w') as f:
    f.write(content)
