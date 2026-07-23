package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.domain.terminal.TerminalService
import com.example.omnimind.ui.theme.*
import kotlinx.coroutines.launch

data class TerminalLine(val text: String, val type: String = "output")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    terminalService: TerminalService,
    onBack: () -> Unit
) {
    var command by remember { mutableStateOf("") }
    var lines by remember { mutableStateOf(listOf(TerminalLine("> Ready", "info"))) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) listState.animateScrollToItem(lines.size - 1)
    }

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TERMINAL", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text("أوامر مباشرة", color = DimText, fontSize = 11.sp)
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
        Column(modifier = Modifier.fillMaxSize().padding(padding).background(VoidBlack)) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF0A0E14))
                    .padding(12.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(lines) { line ->
                    if (line.text.isNotBlank()) {
                        Text(
                            text = line.text,
                            color = when (line.type) {
                                "error" -> SignalRed
                                "success" -> SignalGreen
                                "info" -> ElectricCyan
                                else -> DimText
                            },
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .border(0.5.dp, SteelBorder)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$", color = SignalGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = command,
                    onValueChange = { command = it },
                    placeholder = { Text("اكتب أمر...", color = MutedGrey, fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = SignalGreen,
                        focusedTextColor = RawWhite,
                        unfocusedTextColor = RawWhite
                    )
                )
                IconButton(onClick = {
                    if (command.isNotBlank()) {
                        lines = lines + TerminalLine("$ $command", "input")
                        scope.launch {
                            val output = terminalService.executeCommand(command)
                            if (output.isNotBlank()) {
                                lines = lines + TerminalLine(output, "output")
                            }
                            command = ""
                        }
                    }
                }) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Execute", tint = SignalGreen)
                }
            }
        }
    }
}
