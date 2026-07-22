package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.omnimind.data.model.GitHubRepo
import com.example.omnimind.data.model.GitHubContent
import com.example.omnimind.data.model.GitHubContent
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
            .background(VoidBlack)
    ) {
        if (fileContent != null) {
            val (name, content) = fileContent!!
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(name.uppercase(), fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = SignalGreen) },
                    navigationIcon = {
                        IconButton(onClick = viewModel::clearGitHubFile) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = SignalGreen)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = VoidBlack)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .border(1.dp, SteelBorder)
                        .background(IndustrialGrey)
                        .padding(16.dp)
                ) {
                    Text(
                        text = content,
                        fontFamily = FontFamily.Monospace,
                        color = RawWhite,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            return@Box
        }

        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text_TAG(text = "GIT_REPOSITORY_MANAGER",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = SignalGreen,
                fontSize = 12.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            if (user == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SteelBorder)
                        .background(IndustrialGrey)
                        .padding(32.dp)
                ) {
                    Column {
                        Text("AUTHENTICATION_REQUIRED", color = RawWhite, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(24.dp))
                        TextField(
                            value = tokenInput,
                            onValueChange = { tokenInput = it },
                            placeholder = { Text("GITHUB_TOKEN", textColor = GhostGrey, fontFamily = FontFamily.Monospace) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = VoidBlack,
                                focusedIndicatorColor = SignalGreen,
                                unfocusedIndicatorColor = SteelBorder,
                                textColor = RawWhite
                            ),
                            shape = RectangleShape
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.connectGitHub(tokenInput) },
                            enabled = tokenInput.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SignalGreen),
                            shape = RectangleShape
                        ) {
                            Text("LINK_ACCOUNT", color = VoidBlack, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            } else {
                // Connected State
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = (currentRepo?.second ?: "ROOT").uppercase(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = RawWhite,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
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
                            Text("[BACK]", color = SignalGreen, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (loading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = SignalGreen, trackColor = SteelBorder)
                
                error?.let { Text("ERROR: $it", color = SignalRed, fontFamily = FontFamily.Monospace, fontSize = 12.sp) }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    if (currentRepo == null) {
                        items(repos, key = { it.id }) { repo ->
                            IndustrialRepoCard(repo) {
                                val owner = repo.full_name.substringBefore('/')
                                viewModel.loadGitHubContents(owner, repo.name)
                            }
                        }
                    } else {
                        items(contents, key = { it.path }) { item ->
                            IndustrialContentRow(item, 
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
private fun IndustrialRepoCard(repo: GitHubRepo, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SteelBorder)
            .background(IndustrialGrey)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column {
            Text(repo.full_name.uppercase(), fontWeight = FontWeight.Bold, color = RawWhite, fontFamily = FontFamily.Monospace)
            repo.description?.let { 
                Text(
                    it, 
                    color = GhostGrey, 
                    fontSize = 11.sp, 
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(top = 4.dp)
                ) 
            }
            Row(modifier = Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "[${repo.language.orEmpty().uppercase()}]", 
                    color = SignalGreen, 
                    fontSize = 10.sp, 
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "STARS: ${repo.stargazers_count}", 
                    color = GhostGrey, 
                    fontSize = 10.sp, 
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
private fun IndustrialContentRow(item: GitHubContent, onClick: () -> Unit, onImport: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(IndustrialGrey)
            .border(1.dp, SteelBorder)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (item.type == "dir") "[DIR]" else "[FILE]",
            color = if (item.type == "dir") SignalGreen else GhostGrey,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            item.name, 
            modifier = Modifier.weight(1f).padding(start = 12.dp),
            color = RawWhite,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp
        )
        if (item.type != "dir") {
            IconButton(onClick = onImport, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Download, null, tint = SignalGreen, modifier = Modifier.size(16.dp))
            }
        }
    }
}
