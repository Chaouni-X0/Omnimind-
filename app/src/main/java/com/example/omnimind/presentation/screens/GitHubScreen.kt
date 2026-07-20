package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.omnimind.data.network.GitHubContentItem
import com.example.omnimind.data.network.GitHubRepo
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubScreen(viewModel: OmniMindViewModel) {
    val token by viewModel.gitHubToken.collectAsState()
    val user by viewModel.gitHubUser.collectAsState()
    val repos by viewModel.gitHubRepos.collectAsState()
    val contents by viewModel.gitHubContents.collectAsState()
    val currentRepo by viewModel.gitHubCurrentRepo.collectAsState()
    val currentPath by viewModel.gitHubCurrentPath.collectAsState()
    val fileContent by viewModel.gitHubFileContent.collectAsState()
    val isLoading by viewModel.gitHubLoading.collectAsState()
    val error by viewModel.gitHubError.collectAsState()
    val message by viewModel.gitHubMessage.collectAsState()

    var tokenInput by remember { mutableStateOf(token) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearGitHubMessage()
        }
    }

    // شاشة عرض محتوى ملف
    fileContent?.let { (name, content) ->
        FileViewer(name = name, content = content, onBack = { viewModel.clearGitHubFile() })
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Cloud, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("GitHub", fontWeight = FontWeight.Bold)
                        user?.let {
                            Spacer(Modifier.width(8.dp))
                            Text("(@$it)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            if (user == null) {
                Text(
                    text = "أدخل Personal Access Token من إعدادات GitHub (Developer settings) لعرض مستودعاتك.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = tokenInput,
                    onValueChange = { tokenInput = it },
                    label = { Text("GitHub Token") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.connectGitHub(tokenInput) },
                    enabled = tokenInput.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("اتصال وتحميل المستودعات")
                }
            } else {
                // شريط تنقّل
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentRepo != null) {
                        TextButton(onClick = {
                            if (currentPath.isBlank()) {
                                viewModel.loadGitHubRepos()
                            } else {
                                val parent = currentPath.substringBeforeLast('/', "")
                                viewModel.loadGitHubContents(currentRepo!!.first, currentRepo!!.second, parent)
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("رجوع")
                        }
                    }
                    val label = currentRepo?.let { "${it.second}/$currentPath" } ?: "المستودعات"
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.loadGitHubRepos() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "تحديث")
                    }
                }

                if (currentRepo != null && currentPath.isBlank()) {
                    Button(
                        onClick = { viewModel.cloneRepoToProject("github-import", currentRepo!!.first, currentRepo!!.second) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Icon(Icons.Filled.CloudDownload, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("استنساخ المستودع إلى مشروع")
                    }
                }
            }

            if (isLoading) {
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (currentRepo == null) {
                    items(repos, key = { it.id }) { repo ->
                        RepoCard(repo = repo) { owner, name ->
                            viewModel.loadGitHubContents(owner, name)
                        }
                    }
                } else {
                    items(contents, key = { it.path }) { item ->
                        ContentRow(
                            item = item,
                            onOpenDir = {
                                viewModel.loadGitHubContents(currentRepo!!.first, currentRepo!!.second, item.path)
                            },
                            onOpenFile = { viewModel.openGitHubFile(item) },
                            onImport = { viewModel.importGitHubFileToProject("github-import", item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoCard(repo: GitHubRepo, onOpen: (owner: String, repo: String) -> Unit) {
    val (owner, name) = remember(repo.full_name) {
        val split = repo.full_name.split("/")
        (split.getOrNull(0) ?: "") to (split.getOrNull(1) ?: repo.name)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onOpen(owner, name) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (repo.private) Icons.Filled.Lock else Icons.Filled.Public,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(repo.full_name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
            repo.description?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row(modifier = Modifier.padding(top = 6.dp)) {
                repo.language?.let {
                    Text("● $it", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(12.dp))
                }
                Text("★ ${repo.stargazers_count}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun ContentRow(
    item: GitHubContentItem,
    onOpenDir: () -> Unit,
    onOpenFile: () -> Unit,
    onImport: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (item.type == "dir") onOpenDir() else onOpenFile() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (item.type == "dir") Icons.Filled.Folder else Icons.Filled.InsertDriveFile,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(item.name, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
        if (item.type != "dir") {
            IconButton(onClick = onImport) {
                Icon(Icons.Filled.Download, contentDescription = "استيراد", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileViewer(name: String, content: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text(name, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "رجوع")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        Text(
            text = content,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp)
        )
    }
}
