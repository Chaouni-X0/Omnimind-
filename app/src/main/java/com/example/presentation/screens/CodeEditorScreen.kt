package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.omnimind.domain.editor.CodeEditorService

@Composable
fun CodeEditorScreen(editorService: CodeEditorService) {
    var currentDir by remember { mutableStateOf("") }
    var openFile by remember { mutableStateOf<String?>(null) }
    var fileContent by remember { mutableStateOf("") }
    var files by remember { mutableStateOf(editorService.listFiles(currentDir)) }
    var newFileName by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var draggedFile by remember { mutableStateOf<String?>(null) }

    fun refresh() {
        files = editorService.listFiles(currentDir)
    }

    val filteredFiles = remember(files, searchQuery) {
        if (searchQuery.isBlank()) files else files.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        if (openFile == null) {
            Text(text = "محرر الأكواد",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    placeholder = { Text("اسم ملف جديد (مثال: main.kt)") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    if (newFileName.isNotBlank()) {
                        val path = if (currentDir.isBlank()) newFileName else "$currentDir/$newFileName"
                        editorService.createFile(path)
                        newFileName = ""
                        refresh()
                    }
                }) {
                    Icon(Icons.Filled.Save, contentDescription = "إنشاء", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                placeholder = { Text("بحث وفلترة الملفات") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            draggedFile?.let {
                Text(
                    text = "جار سحب: $it. اضغط على مجلد لنقل الملف إليه.",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn {
                items(filteredFiles, key = { it.relativePath }) { entry ->
                    FileBrowserRow(
                        entry = entry,
                        isDragged = draggedFile == entry.relativePath,
                        onClick = {
                            if (entry.isDirectory) {
                                val source = draggedFile
                                if (source == null) {
                                    currentDir = entry.relativePath
                                } else {
                                    val destination = "${entry.relativePath}/${source.substringAfterLast('/')}"
                                    editorService.moveFile(source, destination)
                                    draggedFile = null
                                }
                                refresh()
                            } else if (draggedFile == null) {
                                openFile = entry.relativePath
                                fileContent = editorService.readFile(entry.relativePath)
                                saved = true
                            }
                        },
                        onLongClick = {
                            if (!entry.isDirectory) {
                                draggedFile = if (draggedFile == entry.relativePath) null else entry.relativePath
                            }
                        }
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                IconButton(onClick = { openFile = null; refresh() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = openFile ?: "",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = {
                    openFile?.let {
                        editorService.writeFile(it, fileContent)
                        saved = true
                    }
                }) {
                    Icon(
                        Icons.Filled.Save,
                        contentDescription = "حفظ",
                        tint = if (saved) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = fileContent,
                onValueChange = { fileContent = it; saved = false },
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileBrowserRow(
    entry: CodeEditorService.FileEntry,
    isDragged: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isDragged) MaterialTheme.colorScheme.tertiaryContainer
                else MaterialTheme.colorScheme.background
            )
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(vertical = 10.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (entry.isDirectory) Icons.Filled.Folder else Icons.Filled.InsertDriveFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = entry.name, color = MaterialTheme.colorScheme.onBackground)
    }
}
