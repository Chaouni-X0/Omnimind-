package com.example.omnimind.presentation.screens.apicenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel

@Composable
fun ApiCenterScreen(viewModel: OmniMindViewModel) {
    val apiKeys by viewModel.allApiKeys.collectAsState()
    var editing by remember { mutableStateOf<ApiKeyConfig?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "إضافة مزود")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Text("مركز المزودات", style = MaterialTheme.typography.headlineSmall) }
            if (apiKeys.isEmpty()) item { Text("لا توجد مزودات. أضف مفتاح API للبدء.") }
            items(apiKeys, key = { it.id }) { config ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(config.providerName, style = MaterialTheme.typography.titleMedium)
                                Text("${config.modelId.ifBlank { "النموذج الافتراضي" }} · المستوى ${config.modelTier}")
                            }
                            Switch(
                                checked = config.isEnabled,
                                onCheckedChange = { viewModel.toggleApiKey(config.id, it) }
                            )
                        }
                        config.lastError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { editing = config; showDialog = true }) { Text("تعديل") }
                            TextButton(
                                onClick = { viewModel.deleteApiKey(config.id) },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) { Text("حذف") }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ProviderDialog(
            existing = editing,
            onDismiss = { showDialog = false; editing = null },
            onConfirm = { provider, key, baseUrl, modelId, tier, priority ->
                val current = editing
                if (current == null) viewModel.addApiKey(provider, key, baseUrl, modelId, tier, priority)
                else viewModel.updateApiKey(
                    current.copy(
                        providerName = provider,
                        baseUrl = baseUrl,
                        modelId = modelId,
                        modelTier = tier,
                        priorityWeight = priority
                    ),
                    key
                )
                showDialog = false
                editing = null
            }
        )
    }
}

@Composable
private fun ProviderDialog(
    existing: ApiKeyConfig?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String?, String, Int, Int) -> Unit
) {
    var provider by remember { mutableStateOf(existing?.providerName ?: "Gemini") }
    var key by remember { mutableStateOf("") }
    var baseUrl by remember { mutableStateOf(existing?.baseUrl.orEmpty()) }
    var modelId by remember { mutableStateOf(existing?.modelId ?: "gemini-2.0-flash") }
    var tier by remember { mutableStateOf(existing?.modelTier ?: 1) }
    var priority by remember { mutableStateOf(existing?.priorityWeight ?: 5) }
    val validUrl = baseUrl.isBlank() || baseUrl.startsWith("https://")
    val keyRequired = existing == null || provider.trim() != existing.providerName
    val valid = provider.isNotBlank() && modelId.isNotBlank() && validUrl && (!keyRequired || key.isNotBlank())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "إضافة مزود" else "تعديل المزود") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(provider, { provider = it }, label = { Text("المزود") })
                OutlinedTextField(
                    key,
                    { key = it },
                    label = { Text(if (keyRequired) "مفتاح API" else "مفتاح جديد (اختياري)") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(baseUrl, { baseUrl = it }, label = { Text("رابط HTTPS اختياري") }, isError = !validUrl)
                OutlinedTextField(modelId, { modelId = it }, label = { Text("معرف النموذج") })
                Text("مستوى النموذج: $tier")
                Slider(tier.toFloat(), { tier = it.toInt() }, valueRange = 1f..3f, steps = 1)
                Text("الأولوية: $priority")
                Slider(priority.toFloat(), { priority = it.toInt() }, valueRange = 1f..10f, steps = 8)
            }
        },
        confirmButton = {
            TextButton(
                enabled = valid,
                onClick = { onConfirm(provider.trim(), key, baseUrl.trim().ifBlank { null }, modelId.trim(), tier, priority) }
            ) { Text("حفظ") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }
    )
}
