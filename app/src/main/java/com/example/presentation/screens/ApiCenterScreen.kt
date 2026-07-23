package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCenterScreen(
    viewModel: OmniMindViewModel,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var provider by remember { mutableStateOf("gemini") }
    var apiKey by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var modelId by remember { mutableStateOf("") }

    val apiKeys by viewModel.allApiKeys.collectAsState()

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("API CENTER", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text("${apiKeys.size} مزود مرتبط", color = DimText, fontSize = 11.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DimText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ElectricCyan,
                contentColor = VoidBlack,
                shape = RoundedCornerShape(12.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "Add Provider") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    Text(
                        "المزودين المربوطين",
                        color = RawWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(apiKeys.size, key = { apiKeys[it].id }) { index ->
                    val key = apiKeys[index]
                    val accentColor = when (key.providerName.lowercase()) {
                        "gemini" -> SignalGreen
                        "openai" -> Color(0xFF10A37F)
                        "anthropic" -> Color(0xFFD4A373)
                        "groq" -> ElectricCyan
                        else -> AmberAccent
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(accentColor)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    key.providerName,
                                    color = RawWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    key.modelId?.let { "Tier ${key.modelTier} • $it" } ?: "Tier ${key.modelTier}",
                                    color = DimText,
                                    fontSize = 11.sp
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Switch(
                                    checked = key.isEnabled,
                                    onCheckedChange = { viewModel.toggleApiKey(key.id, it) },
                                    modifier = Modifier.size(32.dp),
                                    colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan, checkedTrackColor = ElectricCyan.copy(alpha = 0.3f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(16.dp),
            title = { Text("إضافة مزود", color = RawWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
            text = {
                Column {
                    OutlinedTextField(
                        value = provider,
                        onValueChange = { provider = it },
                        label = { Text("المزود", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                            focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                            cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        label = { Text("API Key", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                            focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                            cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = modelId,
                        onValueChange = { modelId = it },
                        label = { Text("Model ID", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                            focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                            cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addApiKey(provider, apiKey, null, modelId, 1, 5)
                    showAddDialog = false
                    provider = ""; apiKey = ""; modelId = ""
                }) {
                    Text("إضافة", color = ElectricCyan, fontFamily = FontFamily.Monospace)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("إلغاء", color = DimText) }
            }
        )
    }
}
