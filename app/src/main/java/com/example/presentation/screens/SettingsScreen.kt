package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.prefs.AppTheme
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    selectedTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    onBack: () -> Unit
) {
    var maxConcurrency by remember { mutableStateOf(4) }
    var autoApprove by remember { mutableStateOf(false) }
    var streamMode by remember { mutableStateOf(true) }
    var budgetLimit by remember { mutableStateOf("5.00") }
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("SETTINGS", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text("إعدادات النظام", color = DimText, fontSize = 11.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DimText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(12.dp))

            // Theme selector
            Card(
                modifier = Modifier.fillMaxWidth().clickable { showThemeDialog = true },
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DarkMode, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("السمة اللونية", color = RawWhite, fontSize = 14.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
                        Text(selectedTheme.name, color = DimText, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Swarm settings
            SettingsCard("إعدادات Swarm") {
                SettingSlider("الحد الأقصى للوكلاء", maxConcurrency, 1, 10) { maxConcurrency = it }
                SettingToggle("الموافقة التلقائية", autoApprove) { autoApprove = it }
                SettingToggle("البث المباشر", streamMode) { streamMode = it }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Budget
            SettingsCard("حد الميزانية") {
                OutlinedTextField(
                    value = budgetLimit,
                    onValueChange = { budgetLimit = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("الحد الشهري ($)", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                        focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                        cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // About
            SettingsCard("حول التطبيق") {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text("الإصدار", color = DimText, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text("1.0.0", color = RawWhite, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text("المطور", color = DimText, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text("OmniMind Team", color = RawWhite, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text("الترخيص", color = DimText, fontSize = 13.sp, modifier = Modifier.weight(1f))
                    Text("Free & Open", color = SignalGreen, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(16.dp),
            title = { Text("اختر السمة", color = RawWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppTheme.entries.forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (theme == selectedTheme) ElectricCyan.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { onThemeChange(theme); showThemeDialog = false }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = theme == selectedTheme,
                                onClick = { onThemeChange(theme); showThemeDialog = false },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(theme.name, color = RawWhite, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
private fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingSlider(label: String, value: Int, min: Int, max: Int, onValueChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = RawWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text("$value", color = ElectricCyan, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = min.toFloat()..max.toFloat(),
        steps = max - min - 1,
        colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
    )
}

@Composable
private fun SettingToggle(label: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = RawWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan, checkedTrackColor = ElectricCyan.copy(alpha = 0.3f))
        )
    }
}
