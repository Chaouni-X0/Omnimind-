package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.prefs.AppTheme
import com.example.omnimind.ui.theme.*

@Composable
fun OnboardingScreen(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onComplete: (apiKey: String, monthlyBudget: Long?) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(VoidBlack, DeepNavy, SurfaceDark)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "OMNIMIND",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = ElectricCyan,
                textAlign = TextAlign.Center
            )
            Text(
                text = "AI POWERED DEVELOPMENT",
                style = MaterialTheme.typography.labelLarge,
                color = DimText,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(2.dp)
                    .background(ElectricCyan, RoundedCornerShape(1.dp))
            )

            Spacer(modifier = Modifier.height(48.dp))

            when (step) {
                0 -> WelcomeStep(onContinue = { step = 1 })
                1 -> ThemeStep(selectedTheme, onThemeSelected) { step = 2 }
                2 -> ApiKeyStep(onComplete)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .width(if (index == step) 32.dp else 8.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (index == step) ElectricCyan else SteelBorder)
                )
            }
        }
    }
}

@Composable
private fun WelcomeStep(onContinue: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Build with AI. Ship faster.",
            style = MaterialTheme.typography.headlineMedium,
            color = RawWhite,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "منصة تطوير مدعومة بالذكاء الاصطناعي تتيح لك بناء مشاريع ضخمة بأسعار مجانية عبر دمج عدة مزودين.",
            style = MaterialTheme.typography.bodyMedium,
            color = DimText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        FeatureItem(icon = "\u26A1", title = "Swarm AI", desc = "عدة نماذج تعمل معاً")
        FeatureItem(icon = "\uD83D\uDD12", title = "100% Local", desc = "مفاتيحك مشفرة على جهازك")
        FeatureItem(icon = "\uD83C\uDF10", title = "Multi-Provider", desc = "Gemini, OpenAI, Anthropic, Groq...")
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ابدأ الآن", color = VoidBlack, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun FeatureItem(icon: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .border(0.5.dp, SteelBorder, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
            Text(text = desc, color = DimText, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ThemeStep(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onContinue: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = "اختر مظهرك", style = MaterialTheme.typography.headlineSmall, color = RawWhite, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "اختر السمة اللونية التي تناسب بيئة عملك", color = DimText, fontSize = 14.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))

        val themes = listOf(
            Triple(AppTheme.OBSIDIAN, SurfaceDark, ElectricCyan),
            Triple(AppTheme.AURORA, Color(0xFF0D1B2A), Color(0xFF00BCD4)),
            Triple(AppTheme.EMBER, Color(0xFF1A0A00), Color(0xFFFF8F00))
        )

        themes.forEach { (theme, bg, accent) ->
            val isSelected = theme == selectedTheme
            val themeName = when (theme) {
                AppTheme.OBSIDIAN -> "Obsidian"
                AppTheme.AURORA -> "Aurora"
                AppTheme.EMBER -> "Ember"
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clip(RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = bg),
                shape = RoundedCornerShape(12.dp),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, accent)
                    else androidx.compose.foundation.BorderStroke(1.dp, SteelBorder)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = themeName, color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                    RadioButton(selected = isSelected, onClick = { onThemeSelected(theme) }, colors = RadioButtonDefaults.colors(selectedColor = accent))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("متابعة", color = VoidBlack, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun ApiKeyStep(onComplete: (apiKey: String, monthlyBudget: Long?) -> Unit) {
    var apiKey by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("gemini") }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = "ربط مزود AI", style = MaterialTheme.typography.headlineSmall, color = RawWhite, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "أضف مفتاح API الخاص بك. يتم تشفيره وحفظه بأمان على جهازك فقط.", color = DimText, fontSize = 13.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))

        val providers = listOf(
            "gemini" to "Google Gemini",
            "openai" to "OpenAI",
            "anthropic" to "Anthropic",
            "groq" to "Groq",
            "openrouter" to "OpenRouter"
        )

        providers.forEach { (id, name) ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                border = androidx.compose.foundation.BorderStroke(if (provider == id) 1.5.dp else 0.5.dp, if (provider == id) ElectricCyan else SteelBorder)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = name, color = if (provider == id) ElectricCyan else RawWhite, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = if (provider == id) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.weight(1f))
                    RadioButton(selected = provider == id, onClick = { provider = id }, colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("مفتاح ${provider.replaceFirstChar { it.uppercase() }} API", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
            ),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = budget,
            onValueChange = { budget = it.filter { c -> c.isDigit() } },
            label = { Text("حد الميزانية الشهرية (\$)", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
            placeholder = { Text("0 = بلا حدود", fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
            ),
            textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "مفاتيحك مشفرة بـ AES-GCM ومحفوظة في Android Keystore", color = SignalGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onComplete(apiKey, budget.toLongOrNull()) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
            shape = RoundedCornerShape(12.dp),
            enabled = apiKey.isNotBlank()
        ) {
            Text("إتمام الإعداد", color = VoidBlack, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
        }
    }
}
