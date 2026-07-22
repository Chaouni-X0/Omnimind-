package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import com.example.omnimind.data.model.GitHubRepo
import com.example.omnimind.data.model.GitHubContent
import com.example.omnimind.ui.theme.*

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
    val loading by viewModel.gitHubLoading.collectAsState()
    val error by viewModel.gitHubError.collectAsState()
    var tokenInput by remember { mutableStateOf(token) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ManusBlack)
    ) {
        if (fileContent != null) {
            val (name, content) = fileContent!!
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = viewModel::clearGitHubFile) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ManusSurface)
                        .padding(12.dp)
                ) {
                    Text(
                        text = content,
                        fontFamily = FontFamily.Monospace,
                        color = ManusTextPrimary,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            return@Box
        }

        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "GITHUB ENGINE",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = ManusElectricBlue,
                letterSpacing = 2.sp
            )
            
            user?.let { 
                Text(
                    text = "@$it", 
                    color = ManusTextSecondary, 
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                ) 
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (user == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ManusSurface)
                        .border(1.dp, ManusBorder, RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        Text("Connect Account", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = tokenInput,
                            onValueChange = { tokenInput = it },
                            placeholder = { Text("GitHub Access Token", color = ManusTextSecondary) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = ManusElectricBlue,
                                unfocusedBorderColor = ManusBorder,
                                color = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.connectGitHub(tokenInput) },
                            enabled = tokenInput.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ManusElectricBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Authorize", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentRepo?.let { "${it.second}/$currentPath" } ?: "Repositories",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    if (currentRepo != null) {
                        TextButton(onClick = {
                            if (currentPath.isBlank()) viewModel.loadGitHubRepos()
                            else viewModel.loadGitHubContents(
                                currentRepo!!.first,
                                currentRepo!!.second,
                                currentPath.substringBeforeLast('/', "")
                            )
                        }) {
                            Text("Go Back", color = ManusElectricBlue)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (loading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ManusElectricBlue)
                    }
                }
                
                error?.let { Text(it, color = Color.Red, fontSize = 12.sp) }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentRepo == null) {
                        items(repos, key = { it.id }) { repo ->
                            RepoCard(repo) {
                                val owner = repo.full_name.substringBefore('/')
                                viewModel.loadGitHubContents(owner, repo.name)
                            }
                        }
                    } else {
                        items(contents, key = { it.path }) { item ->
                            ContentItem(item, 
                                onClick = {
                                    if (item.type == "dir") viewModel.loadGitHubContents(currentRepo!!.first, currentRepo!!.second, item.path)
                                    else viewModel.openGitHubFile(item)
                                },
                                onImport = { viewModel.importGitHubFileToProject("github-import", item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoCard(repo: GitHubRepo, onClick: () -> Unit) { // Simplified type for brevity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ManusSurface)
            .border(1.dp, ManusBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Book, null, tint = ManusElectricBlue, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(repo.full_name, fontWeight = FontWeight.Bold, color = Color.White)
            }
            repo.description?.let { 
                Text(
                    it, 
                    color = ManusTextSecondary, 
                    fontSize = 13.sp, 
                    maxLines = 2,
                    modifier = Modifier.padding(top = 4.dp)
                ) 
            }
            Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(ManusElectricBlue))
                Spacer(modifier = Modifier.width(4.dp))
                Text(repo.language.orEmpty(), color = ManusTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Star, null, tint = Color.Yellow, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${repo.stargazers_count}", color = ManusTextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ContentItem(item: GitHubContent, onClick: () -> Unit, onImport: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ManusSurface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (item.type == "dir") Icons.Filled.Folder else Icons.Filled.Description, 
            null, 
            tint = if (item.type == "dir") ManusElectricBlue else ManusTextSecondary
        )
        Text(
            item.name, 
            modifier = Modifier.weight(1f).padding(start = 12.dp),
            color = Color.White,
            fontSize = 14.sp
        )
        if (item.type != "dir") {
            IconButton(onClick = onImport, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Download, null, tint = ManusElectricBlue, modifier = Modifier.size(18.dp))
            }
        }
    }
}
