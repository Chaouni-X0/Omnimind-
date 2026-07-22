package com.example.omnimind.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel

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

    fileContent?.let { (name, content) ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row {
                IconButton(onClick = viewModel::clearGitHubFile) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "رجوع")
                }
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Text(content, fontFamily = FontFamily.Monospace, modifier = Modifier.fillMaxSize())
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("GitHub", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        user?.let { Text("@$it") }
        if (user == null) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = tokenInput,
                onValueChange = { tokenInput = it },
                label = { Text("Personal Access Token") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.connectGitHub(tokenInput) },
                enabled = tokenInput.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("اتصال") }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                if (currentRepo != null) {
                    Button(onClick = {
                        if (currentPath.isBlank()) viewModel.loadGitHubRepos()
                        else viewModel.loadGitHubContents(
                            currentRepo!!.first,
                            currentRepo!!.second,
                            currentPath.substringBeforeLast('/', "")
                        )
                    }) { Text("رجوع") }
                }
                Text(currentRepo?.let { "${it.second}/$currentPath" } ?: "المستودعات")
            }
            if (currentRepo != null && currentPath.isBlank()) {
                Button(
                    onClick = { viewModel.cloneRepoToProject("github-import", currentRepo!!.first, currentRepo!!.second) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("استنساخ إلى مساحة العمل") }
            }
        }
        if (loading) CircularProgressIndicator()
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (currentRepo == null) {
                items(repos, key = { it.id }) { repo ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable {
                            val owner = repo.full_name.substringBefore('/')
                            viewModel.loadGitHubContents(owner, repo.name)
                        }
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(repo.full_name, fontWeight = FontWeight.Bold)
                            repo.description?.let { Text(it) }
                            Text("${if (repo.`private`) "Private" else "Public"} · ${repo.language.orEmpty()} · ★ ${repo.stargazers_count}")
                        }
                    }
                }
            } else {
                items(contents, key = { it.path }) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            if (item.type == "dir") viewModel.loadGitHubContents(currentRepo!!.first, currentRepo!!.second, item.path)
                            else viewModel.openGitHubFile(item)
                        }.padding(vertical = 10.dp)
                    ) {
                        Icon(if (item.type == "dir") Icons.Filled.Folder else Icons.Filled.InsertDriveFile, null)
                        Text(item.name, modifier = Modifier.weight(1f).padding(start = 8.dp))
                        if (item.type != "dir") {
                            Button(onClick = { viewModel.importGitHubFileToProject("github-import", item) }) { Text("استيراد") }
                        }
                    }
                }
            }
        }
    }
}
