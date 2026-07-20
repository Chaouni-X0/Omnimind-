package com.example.omnimind.presentation.screens.apicenter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCenterScreen(viewModel: OmniMindViewModel) {
    val enabledKeys by viewModel.enabledApiKeys.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var editingKey by remember { mutableStateOf<ApiKeyConfig?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مركز المزودات", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "إضافة مزود")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "المزودات المفعلة (${enabledKeys.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (enabledKeys.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.Cloud,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "لا توجد مزودات مفعلة",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "اضغط على + لإضافة مزود جديد",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            } else {
                items(enabledKeys) { apiKey ->
                    ProviderCard(
                        apiKey = apiKey,
                        onEdit = { editingKey = apiKey },
                        onToggle = { viewModel.toggleApiKey(apiKey.id, !apiKey.isEnabled) },
                        onDelete = { viewModel.deleteApiKey(apiKey.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddProviderDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { provider, key, baseUrl, priority ->
                viewModel.addApiKey(provider, key, baseUrl, priority)
                showAddDialog = false
            }
        )
    }

    editingKey?.let { key ->
        EditProviderDialog(
            apiKey = key,
            onDismiss = { editingKey = null },
            onConfirm = { provider, newKey, baseUrl, priority ->
                viewModel.updateApiKey(key.copy(
                    providerName = provider,
                    encryptedKey = newKey,
                    baseUrl = baseUrl,
                    priorityWeight = priority
                ))
                editingKey = null
            }
        )
    }
}

@Composable
private fun ProviderCard(
    apiKey: ApiKeyConfig,
    onEdit: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val providerColor = when (apiKey.providerName.lowercase()) {
        "gemini" -> Color(0xFF4285F4)
        "openai" -> Color(0xFF10A37F)
        "anthropic" -> Color(0xFFD97757)
        "github" -> Color(0xFF6E5494)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(providerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = apiKey.providerName.take(2).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = apiKey.providerName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "الأولوية: ${apiKey.priorityWeight}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Switch(
                    checked = apiKey.isEnabled,
                    onCheckedChange = { onToggle() }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (apiKey.baseUrl != null) {
                Text(
                    text = "الرابط: ${apiKey.baseUrl}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تعديل")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("حذف")
                }
            }
        }
    }
}

@Composable
private fun AddProviderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String?, Int) -> Unit
) {
    var provider by remember { mutableStateOf("Gemini") }
    var apiKey by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(5) }

    val providers = listOf("Gemini", "OpenAI", "Anthropic", "Custom")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة مزود جديد") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("المزود", style = MaterialTheme.typography.labelMedium)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = provider,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        providers.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    provider = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("مفتاح API") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (provider == "Custom") {
                    OutlinedTextField(
                        value = baseUrl,
                        onValueChange = { baseUrl = it },
                        label = { Text("الرابط الأساسي (اختياري)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("https://api.example.com") }
                    )
                }

                Text("الأولوية: $priority", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = priority.toFloat(),
                    onValueChange = { priority = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(provider, apiKey, baseUrl.takeIf { it.isNotBlank() }, priority) },
                enabled = apiKey.isNotBlank()
            ) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

@Composable
private fun EditProviderDialog(
    apiKey: ApiKeyConfig,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String?, Int) -> Unit
) {
    var provider by remember { mutableStateOf(apiKey.providerName) }
    var key by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf(apiKey.baseUrl ?: "") }
    var priority by remember { mutableStateOf(apiKey.priorityWeight) }

    val providers = listOf("Gemini", "OpenAI", "Anthropic", "Custom")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("تعديل المزود") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("المزود", style = MaterialTheme.typography.labelMedium)
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = provider,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        providers.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    provider = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = key,
                    onValueChange = { key = it },
                    label = { Text("مفتاح API جديد (اختياري)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("اتركه فارغاً للإبقاء على المفتاح الحالي") }
                )

                OutlinedTextField(
                    value = baseUrl,
                    onValueChange = { baseUrl = it },
                    label = { Text("الرابط الأساسي (اختياري)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("الأولوية: $priority", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = priority.toFloat(),
                    onValueChange = { priority = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val finalKey = if (key.isBlank()) apiKey.encryptedKey else key
                    onConfirm(provider, finalKey, baseUrl.takeIf { it.isNotBlank() }, priority)
                }
            ) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}