import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

if 'import com.example.presentation.screens.apicenter.ApiCenterScreen' not in content:
    content = content.replace('import com.example.presentation.navigation.Screen\n', 'import com.example.presentation.navigation.Screen\nimport com.example.presentation.screens.apicenter.ApiCenterScreen\n')

content = content.replace('onNavigateToApiCenter = { /* TODO */ },', 'onNavigateToApiCenter = { currentScreen = Screen.ApiCenter },')

content = content.replace("""is Screen.ApiCenter -> {
                            // TODO
                        }""", """is Screen.ApiCenter -> {
                            ApiCenterScreen(viewModel = viewModel, onNavigateBack = { currentScreen = Screen.Dashboard })
                        }""")

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
