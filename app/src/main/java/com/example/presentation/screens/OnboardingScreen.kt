package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.omnimind.R
import com.example.omnimind.data.prefs.AppTheme

private enum class OnboardingStep { WELCOME, THEME, API_KEY }

@Composable
fun OnboardingScreen(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onComplete: (apiKey: String, monthlyBudget: Long?) -> Unit
) {
    var step by remember { mutableStateOf(OnboardingStep.WELCOME) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when (step) {
            OnboardingStep.WELCOME -> WelcomeStep(onNext = { step = OnboardingStep.THEME })
            OnboardingStep.THEME -> ThemeStep(
                selectedTheme = selectedTheme,
                onThemeSelected = onThemeSelected,
                onNext = { step = OnboardingStep.API_KEY }
            )
            OnboardingStep.API_KEY -> ApiKeyStep(onComplete = onComplete)
        }
    }
}

@Composable
private fun WelcomeStep(onNext: () -> Unit) {
    Text(
        text = stringResource(R.string.welcome_title),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        textColor = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.welcome_subtitle),
        style = MaterialTheme.typography.bodyLarge,
        textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(stringResource(R.string.begin_setup))
    }
}

@Composable
private fun ThemeStep(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onNext: () -> Unit
) {
    Text(
        text = stringResource(R.string.choose_environment),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        textColor = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.choose_environment_desc),
        style = MaterialTheme.typography.bodyMedium,
        textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )
    Spacer(modifier = Modifier.height(24.dp))

    ThemeOption(
        name = stringResource(R.string.theme_obsidian),
        description = stringResource(R.string.theme_obsidian_desc),
        swatch = Color(0xFF9B7BFF),
        selected = selectedTheme == AppTheme.OBSIDIAN,
        onClick = { onThemeSelected(AppTheme.OBSIDIAN) }
    )
    Spacer(modifier = Modifier.height(12.dp))
    ThemeOption(
        name = stringResource(R.string.theme_aurora),
        description = stringResource(R.string.theme_aurora_desc),
        swatch = Color(0xFF00B8A9),
        selected = selectedTheme == AppTheme.AURORA,
        onClick = { onThemeSelected(AppTheme.AURORA) }
    )
    Spacer(modifier = Modifier.height(12.dp))
    ThemeOption(
        name = stringResource(R.string.theme_ember),
        description = stringResource(R.string.theme_ember_desc),
        swatch = Color(0xFFFF8A50),
        selected = selectedTheme == AppTheme.EMBER,
        onClick = { onThemeSelected(AppTheme.EMBER) }
    )

    Spacer(modifier = Modifier.height(32.dp))
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(stringResource(R.string.theme_continue_btn))
    }
}

@Composable
private fun ThemeOption(
    name: String,
    description: String,
    swatch: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                width = if (selected) 2.dp else 1.dp,
                textColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .background(swatch, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = name, fontWeight = FontWeight.Bold, textColor = MaterialTheme.colorScheme.onSurface)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ApiKeyStep(onComplete: (apiKey: String, monthlyBudget: Long?) -> Unit) {
    var apiKey by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    Text(
        text = stringResource(R.string.connect_intelligence),
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        textColor = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.connect_intelligence_desc),
        style = MaterialTheme.typography.bodyMedium,
        textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    )
    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
        value = apiKey,
        onValueChange = { apiKey = it },
        label = { Text(stringResource(R.string.gemini_api_key)) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = budget,
        onValueChange = { budget = it.filter { c -> c.isDigit() } },
        label = { Text(stringResource(R.string.monthly_budget)) },
        placeholder = { Text(stringResource(R.string.budget_unlimited)) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.key_security_note),
        style = MaterialTheme.typography.bodySmall,
        textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
    )
    Spacer(modifier = Modifier.height(24.dp))
    Button(
        onClick = { onComplete(apiKey, budget.toLongOrNull()) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(stringResource(R.string.complete_setup))
    }
}
