package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Save
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
import com.example.omnimind.domain.editor.CodeEditorService
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    editorService: CodeEditorService,
    onBack: () -> Unit
) {
    var files by remember { mutableStateOf(editorService.listFiles("")) }
    var selectedFile by remember { mutableStateOf<String?>(null) }
    var codeText by remember { mutableStateOf("") }
    var isReadOnly by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("EDITOR", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text(selectedFile?.substringAfterLast("/") ?: "محرر الأكواد", color = ElectricCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedFile != null) {
                            selectedFile = null
                            codeText = ""
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DimText)
                    }
                },
                actions = {
                    if (selectedFile != null) {
                        IconButton(onClick = { isReadOnly = !isReadOnly }) {
                            Icon(Icons.Default.Description, contentDescription = "Toggle Edit", tint = if (isReadOnly) DimText else ElectricCyan)
                        }
                        IconButton(onClick = {
                            selectedFile?.let {
                                editorService.writeFile(it, codeText)
                                isReadOnly = true
                            }
                        }) {
                            Icon(Icons.Default.Save, contentDescription = "Save", tint = ElectricCyan)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (selectedFile == null) {
                // File browser
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF0A0E14))
                        .border(0.5.dp, SteelBorder),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(files) { _, entry ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceDark)
                                .padding(12.dp)
                                .background(if (entry.isDirectory) ElectricCyan.copy(alpha = 0.05f) else Color.Transparent),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (entry.isDirectory) "[DIR]" else "[FILE]",
                                color = if (entry.isDirectory) SignalGreen else DimText,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                entry.name,
                                modifier = Modifier.weight(1f).padding(start = 12.dp),
                                color = RawWhite,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            if (!entry.isDirectory) {
                                IconButton(onClick = {
                                    selectedFile = entry.relativePath
                                    codeText = editorService.readFile(entry.relativePath)
                                }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Description, null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            } else {
                // Code area
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFF0A0E14))
                        .border(0.5.dp, SteelBorder),
                    state = listState,
                    contentPadding = PaddingValues(12.dp)
                ) {
                    itemsIndexed(codeText.lines()) { index, line ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("${index + 1}", color = MutedGrey, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.width(32.dp).padding(end = 8.dp))
                            Text(
                                line,
                                color = if (isReadOnly) RawWhite else ElectricCyan,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Status bar
                Row(
                    modifier = Modifier.fillMaxWidth().background(SurfaceDark).border(0.5.dp, SteelBorder).padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kotlin", color = SignalGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("${codeText.lines().size} lines", color = DimText, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(if (isReadOnly) "Read Only" else "Editing", color = if (isReadOnly) DimText else AmberAccent, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}
