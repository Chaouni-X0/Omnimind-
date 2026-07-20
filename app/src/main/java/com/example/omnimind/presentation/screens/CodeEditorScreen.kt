package com.example.omnimind.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.omnimind.domain.editor.CodeEditorService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(editorService: CodeEditorService) {
    var currentDir by remember { mutableStateOf("") }
    var openFile by remember { mutableStateOf<String?>(null) }
    var fileValue by remember { mutableStateOf(TextFieldValue("")) }
    var files by remember { mutableStateOf(editorService.listFiles(currentDir)) }
    var newFileName by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(true) }
    var showNewFileDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // حالة السحب والإفلات
    var draggedFile by remember { mutableStateOf<String?>(null) }

    val clipboard = LocalClipboardManager.current

    fun refresh() {
        files = editorService.listFiles(currentDir)
    }

    val filteredFiles = remember(files, searchQuery) {
        if (searchQuery.isBlank()) files
        else files.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Code, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "محرر الأكواد",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            actions = {
                if (openFile != null) {
                    IconButton(onClick = { openFile = null; refresh() }) {
                        Icon(Icons.Filled.FolderOpen, contentDescription = "الملفات")
                    }
                }
                IconButton(onClick = { showNewFileDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "ملف جديد")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        if (openFile == null) {
            // شريط البحث/الفلترة
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("بحث وفلترة الملفات") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Filled.Clear, contentDescription = "مسح")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )

            if (draggedFile != null) {
                Text(
                    text = "🟢 جارٍ سحب: $draggedFile — اضغط على مجلد لنقله إليه، أو اضغط مطولًا مرة أخرى للإلغاء",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            FileBrowser(
                files = filteredFiles,
                currentDir = currentDir,
                editorService = editorService,
                draggedFile = draggedFile,
                onDirectoryClick = { dir ->
                    val dropTarget = draggedFile
                    if (dropTarget != null) {
                        // إفلات: نقل الملف المسحوب إلى هذا المجلد
                        val name = dropTarget.substringAfterLast('/')
                        val dest = if (dir.isBlank()) name else "$dir/$name"
                        editorService.moveFile(dropTarget, dest)
                        draggedFile = null
                        refresh()
                    } else {
                        currentDir = dir
                        refresh()
                    }
                },
                onFileClick = { file ->
                    if (draggedFile == null) {
                        openFile = file
                        fileValue = TextFieldValue(editorService.readFile(file))
                        saved = true
                    }
                },
                onStartDrag = { path -> draggedFile = if (draggedFile == path) null else path }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                EditorToolbar(
                    fileName = openFile ?: "",
                    isSaved = saved,
                    onSave = {
                        openFile?.let {
                            editorService.writeFile(it, fileValue.text)
                            saved = true
                        }
                    },
                    onCut = {
                        val sel = fileValue.selection
                        if (!sel.collapsed) {
                            val selected = fileValue.text.substring(sel.min, sel.max)
                            clipboard.setText(AnnotatedString(selected))
                            val newText = fileValue.text.removeRange(sel.min, sel.max)
                            fileValue = TextFieldValue(
                                text = newText,
                                selection = androidx.compose.ui.text.TextRange(sel.min)
                            )
                            saved = false
                        }
                    },
                    onCopy = {
                        val sel = fileValue.selection
                        if (!sel.collapsed) {
                            clipboard.setText(AnnotatedString(fileValue.text.substring(sel.min, sel.max)))
                        } else {
                            clipboard.setText(AnnotatedString(fileValue.text))
                        }
                    },
                    onPaste = {
                        val pasteText = clipboard.getText()?.text ?: ""
                        if (pasteText.isNotEmpty()) {
                            val sel = fileValue.selection
                            val newText = fileValue.text.replaceRange(sel.min, sel.max, pasteText)
                            fileValue = TextFieldValue(
                                text = newText,
                                selection = androidx.compose.ui.text.TextRange(sel.min + pasteText.length)
                            )
                            saved = false
                        }
                    }
                )
                TextField(
                    value = fileValue,
                    onValueChange = {
                        fileValue = it
                        saved = false
                    },
                    label = { Text("محتوى الملف") },
                    placeholder = { Text("ابدأ بالكتابة هنا...") },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(autoCorrect = false),
                    textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace)
                )
            }
        }
    }

    if (showNewFileDialog) {
        AlertDialog(
            onDismissRequest = { showNewFileDialog = false },
            title = { Text("إنشاء ملف جديد") },
            text = {
                OutlinedTextField(
                    value = newFileName,
                    onValueChange = { newFileName = it },
                    label = { Text("اسم الملف") },
                    placeholder = { Text("مثال: main.kt") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newFileName.isNotBlank()) {
                            val path = if (currentDir.isBlank()) newFileName else "$currentDir/$newFileName"
                            editorService.createFile(path)
                            newFileName = ""
                            showNewFileDialog = false
                            refresh()
                        }
                    },
                    enabled = newFileName.isNotBlank()
                ) {
                    Text("إنشاء")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewFileDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileBrowser(
    files: List<CodeEditorService.FileEntry>,
    currentDir: String,
    editorService: CodeEditorService,
    draggedFile: String?,
    onDirectoryClick: (String) -> Unit,
    onFileClick: (String) -> Unit,
    onStartDrag: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (currentDir.isBlank()) "ملفات المشروع" else "/$currentDir",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (currentDir.isNotBlank()) {
                TextButton(onClick = { onDirectoryClick(currentDir.substringBeforeLast('/', "")) }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("رجوع")
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = files, key = { it.relativePath }) { entry ->
                FileItem(
                    entry = entry,
                    isDragged = entry.relativePath == draggedFile,
                    isDropTarget = draggedFile != null && entry.isDirectory,
                    language = editorService.detectLanguage(entry.name),
                    onClick = {
                        if (entry.isDirectory) onDirectoryClick(entry.relativePath)
                        else onFileClick(entry.relativePath)
                    },
                    onLongPress = { if (!entry.isDirectory) onStartDrag(entry.relativePath) }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileItem(
    entry: CodeEditorService.FileEntry,
    isDragged: Boolean,
    isDropTarget: Boolean,
    language: String,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    val bg = when {
        isDragged -> MaterialTheme.colorScheme.tertiaryContainer
        isDropTarget && entry.isDirectory -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = if (entry.isDirectory) Icons.Filled.Folder else Icons.Filled.InsertDriveFile,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = entry.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!entry.isDirectory) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "($language)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (isDragged) {
            Icon(Icons.Filled.DragIndicator, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        } else if (!entry.isDirectory) {
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun EditorToolbar(
    fileName: String,
    isSaved: Boolean,
    onSave: () -> Unit,
    onCut: () -> Unit,
    onCopy: () -> Unit,
    onPaste: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Description, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = fileName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (isSaved) "محفوظ" else "غير محفوظ",
                style = MaterialTheme.typography.bodySmall,
                color = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(onClick = onSave, enabled = !isSaved) {
                Icon(Icons.Filled.Save, contentDescription = "حفظ", tint = MaterialTheme.colorScheme.primary)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AssistChip(onClick = onCut, label = { Text("قص") }, leadingIcon = { Icon(Icons.Filled.ContentCut, null, Modifier.size(16.dp)) })
            AssistChip(onClick = onCopy, label = { Text("نسخ") }, leadingIcon = { Icon(Icons.Filled.ContentCopy, null, Modifier.size(16.dp)) })
            AssistChip(onClick = onPaste, label = { Text("لصق") }, leadingIcon = { Icon(Icons.Filled.ContentPaste, null, Modifier.size(16.dp)) })
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}
