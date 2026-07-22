package com.example.omnimind.presentation.screens.apicenter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
            .background(ManusBlack)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true },
                    containerColor = ManusElectricBlue,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Provider", tint = Color.White)
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
            ) {
                item {
                    Text(
                        text = "API ENGINE CENTER",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ManusElectricBlue,
                        letterSpacing = 2.sp
                    )
                }
                
                if (apiKeys.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(ManusSurface)
                                .border(1.dp, ManusBorder, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No providers configured.", color = ManusTextSecondary)
                        }
                    }
                }
                
                items(apiKeys, key = { it.id }) { config ->
                    ProviderCard(
                        config = config,
                        onToggle = { viewModel.toggleApiKey(config.id, it) },
                        onEdit = { editing = config; showDialog = true },
                        onDelete = { viewModel.deleteApiKey(config.id) }
                    )
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
}

@Composable
private fun ProviderCard(
    config: ApiKeyConfig,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ManusSurface)
            .border(1.dp, ManusBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(GlassWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Dns, null, tint = ManusElectricBlue, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(config.providerName, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            "${config.modelId.ifBlank { "Default" }} · Tier ${config.modelTier}",
                            fontSize = 12.sp,
                            color = ManusTextSecondary
                        )
                    }
                }
                Switch(
                    checked = config.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = ManusElectricBlue,
                        uncheckedThumbColor = ManusTextSecondary,
                        uncheckedTrackColor = ManusBorder
                    )
                )
            }
            
            config.lastError?.let { 
                Text(
                    text = it, 
                    color = Color.Red, 
                    fontSize = 11.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x1AFF0000))
                        .padding(8.dp)
                ) 
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("Edit", color = ManusElectricBlue, fontSize = 13.sp)
                }
                TextButton(onClick = onDelete) {
                    Text("Delete", color = Color.Red.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ManusSurface,
        titleContentColor = Color.White,
        textContentColor = ManusTextSecondary,
        title = { Text(if (existing == null) "ADD PROVIDER" else "EDIT PROVIDER", fontSize = 16.sp, fontWeight = FontWeight.Black) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ManusTextField(value = provider, onValueChange = { provider = it }, label = "Provider Name")
                ManusTextField(
                    value = key, 
                    onValueChange = { key = it }, 
                    label = if (existing == null) "API Key" else "New API Key (Optional)",
                    isPassword = true
                )
                ManusTextField(value = baseUrl, onValueChange = { baseUrl = it }, label = "Custom Endpoint (Optional)")
                ManusTextField(value = modelId, onValueChange = { modelId = it }, label = "Model Identifier")
                
                Spacer(modifier = Modifier.height(8.dp))
                Text("Model Tier: $tier", color = Color.White, fontSize = 12.sp)
                Slider(
                    value = tier.toFloat(), 
                    onValueChange = { tier = it.toInt() }, 
                    valueRange = 1f..3f, 
                    steps = 1,
                    colors = SliderDefaults.colors(thumbColor = ManusElectricBlue, activeTrackColor = ManusElectricBlue)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(provider.trim(), key, baseUrl.trim().ifBlank { null }, modelId.trim(), tier, priority) },
                colors = ButtonDefaults.buttonColors(containerColor = ManusElectricBlue),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Save Engine") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = ManusTextSecondary) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManusTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 12.sp) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = ManusBlack,
            focusedIndicatorColor = ManusElectricBlue,
            unfocusedIndicatorColor = ManusBorder,
            textColor = Color.White,
            focusedLabelColor = ManusElectricBlue,
            unfocusedLabelColor = ManusTextSecondary
        ),
        shape = RoundedCornerShape(8.dp)
    )
}
