package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.omnimind.domain.terminal.TerminalService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(terminalService: TerminalService) {
    val history = remember { mutableStateListOf<TerminalLine>() }
    var input by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf(terminalService.currentPath()) }
    val listState = rememberLazyListState()
    val commandHistory = remember { mutableStateListOf<String>() }
    var historyIndex by remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        history.add(
            TerminalLine("", "مرحبًا بك في الترمنال الحقيقي. اكتب help لعرض الأوامر المتاحة.", false)
        )
    }

    fun runCommand(rawCmd: String = input) {
        val cmd = rawCmd.trim()
        if (cmd.isEmpty()) return
        commandHistory.add(cmd)
        historyIndex = commandHistory.size
        history.add(TerminalLine(prompt, cmd, true))
        if (cmd == "clear") {
            history.clear()
        } else {
            val output = terminalService.executeCommand(cmd)
            if (output.isNotBlank()) {
                history.add(TerminalLine("", output, false))
            }
        }
        prompt = terminalService.currentPath()
        input = ""
    }

    fun navigateHistory(up: Boolean) {
        if (commandHistory.isEmpty()) return
        if (up) {
            historyIndex = (historyIndex - 1).coerceAtLeast(0)
        } else {
            historyIndex = (historyIndex + 1).coerceAtMost(commandHistory.size)
        }
        input = commandHistory.getOrNull(historyIndex) ?: ""
    }

    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) listState.animateScrollToItem(history.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Terminal,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "الترمنال",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF101418))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(history, key = { it.hashCode() }) { line ->
                TerminalLineView(line = line)
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$prompt \$",
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            IconButton(onClick = { navigateHistory(up = true) }) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "الأمر السابق")
            }
            IconButton(onClick = { navigateHistory(up = false) }) {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "الأمر التالي")
            }
            IconButton(onClick = { runCommand() }, enabled = input.isNotBlank()) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "تنفيذ",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        QuickCommandsRow(onCommandSelected = { cmd -> runCommand(cmd) })
    }
}

data class TerminalLine(
    val prompt: String,
    val content: String,
    val isCommand: Boolean
)

@Composable
private fun TerminalLineView(line: TerminalLine) {
    val textColor = if (line.isCommand) Color(0xFF64B5F6) else Color(0xFFE0E0E0)
    Text(
        text = if (line.isCommand) "${line.prompt} $ ${line.content}" else line.content,
        fontFamily = FontFamily.Monospace,
        color = textColor,
        style = MaterialTheme.typography.bodySmall
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickCommandsRow(onCommandSelected: (String) -> Unit) {
    val quickCommands = listOf("help", "ls", "pwd", "history", "clear")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        quickCommands.forEach { cmd ->
            AssistChip(
                onClick = { onCommandSelected(cmd) },
                label = { Text(cmd) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}
