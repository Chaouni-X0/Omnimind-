package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.omnimind.domain.terminal.TerminalService
import kotlinx.coroutines.launch

@Composable
fun TerminalScreen(terminalService: TerminalService) {
    val history = remember { mutableStateListOf("مرحبًا بك في الترمنال. اكتب help لعرض الأوامر المتاحة.") }
    var input by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf(terminalService.currentPath()) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    fun runCommand() {
        val cmd = input.trim()
        if (cmd.isEmpty()) return
        history.add("$prompt \$ $cmd")
        if (cmd == "clear") {
            history.clear()
        } else {
            scope.launch {
                val output = terminalService.executeCommand(cmd)
                if (output.isNotBlank()) history.add(output)
                prompt = terminalService.currentPath()
            }
        }
        input = ""
    }

    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) listState.animateScrollToItem(history.size - 1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(history) { line ->
                Text(
                    text = line,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text_TAG(text = "$prompt \$",
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 6.dp)
            )
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                singleLine = true
            )
            IconButton(onClick = { runCommand() }) {
                Icon(Icons.Filled.Send, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
