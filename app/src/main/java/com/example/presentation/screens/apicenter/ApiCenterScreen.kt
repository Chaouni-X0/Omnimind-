package com.example.omnimind.presentation.screens.apicenter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.model.ApiKeyConfig
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCenterScreen(viewModel: OmniMindViewModel) {
    val apiKeys by viewModel.allApiKeys.collectAsState()
    var editing by remember { mutableStateOf<ApiKeyConfig?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = SignalGreen,
                    shape = RectangleShape
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = VoidBlack)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "API_ENGINE_CENTER",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = SignalGreen,
                    fontSize = 12.sp
                )
                Text(
                    text = "CONFIG",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    color = RawWhite,
                    fontFamily = FontFamily.Monospace
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    if (apiKeys.isEmpty()) {
                        item {
                            Text(
                                "NULL_PROVIDERS_ACTIVE",
                                color = GhostGrey,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                    }
                    
                    items(apiKeys, key = { it.id }) { config ->
                        IndustrialProviderCard(
                            config = config,
                            onToggle = { viewModel.toggleApiKey(config.id, it) },
                            onEdit = { editing = config; showDialog = true },
                            onDelete = { viewModel.deleteApiKey(config.id) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            IndustrialProviderDialog(
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
}

@Composable
private fun IndustrialProviderCard(
    config: ApiKeyConfig,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SteelBorder)
            .background(IndustrialGrey)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        config.providerName.uppercase(), 
                        fontWeight = FontWeight.Bold, 
                        color = RawWhite,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "ID: ${config.modelId.ifBlank { "DEFAULT" }}",
                        fontSize = 10.sp,
                        color = GhostGrey,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Switch(
                    checked = config.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = VoidBlack,
                        checkedTrackColor = SignalGreen,
                        uncheckedThumbColor = GhostGrey,
                        uncheckedTrackColor = SteelBorder
                    )
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "TIER: ${config.modelTier}",
                    color = SignalGreen,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
                Row {
                    TextButton(onClick = onEdit) {
                        Text("[EDIT]", color = SignalGreen, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                    TextButton(onClick = onDelete) {
                        Text("[PURGE]", color = SignalRed, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IndustrialProviderDialog(
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
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = IndustrialGrey,
        shape = RectangleShape,
        title = { 
            Text(
                if (existing == null) "INIT_NEW_PROVIDER" else "MOD_PROVIDER", 
                fontSize = 16.sp, 
                fontWeight = FontWeight.Black,
                color = SignalGreen,
                fontFamily = FontFamily.Monospace
            ) 
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IndustrialTextField(value = provider, onValueChange = { provider = it }, label = "PROVIDER_ID")
                IndustrialTextField(
                    value = key, 
                    onValueChange = { key = it }, 
                    label = if (existing == null) "API_KEY" else "NEW_KEY_OPT",
                    isPassword = true
                )
                IndustrialTextField(value = baseUrl, onValueChange = { baseUrl = it }, label = "ENDPOINT_URL")
                IndustrialTextField(value = modelId, onValueChange = { modelId = it }, label = "MODEL_REF")
                
                Column {
                    Text("TIER_LEVEL: $tier", color = RawWhite, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Slider(
                        value = tier.toFloat(), 
                        onValueChange = { tier = it.toInt() }, 
                        valueRange = 1f..3f, 
                        steps = 1,
                        colors = SliderDefaults.colors(thumbColor = SignalGreen, activeTrackColor = SignalGreen)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(provider.trim(), key, baseUrl.trim().ifBlank { null }, modelId.trim(), tier, priority) },
                colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
                shape = RectangleShape
            ) { Text("SAVE_CONFIG", color = VoidBlack, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { 
                Text("ABORT", color = GhostGrey, fontFamily = FontFamily.Monospace) 
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IndustrialTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 10.sp, fontFamily = FontFamily.Monospace) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = VoidBlack,
            focusedIndicatorColor = SignalGreen,
            unfocusedIndicatorColor = SteelBorder,
            textColor = RawWhite,
            focusedLabelColor = SignalGreen,
            unfocusedLabelColor = GhostGrey
        ),
        shape = RectangleShape
    )
}
