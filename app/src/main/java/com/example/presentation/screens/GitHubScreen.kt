package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.presentation.viewmodel.OmniMindViewModel
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubScreen(
    viewModel: OmniMindViewModel,
    onBack: () -> Unit
) {
    var tokenInput by remember { mutableStateOf("") }
    val gitHubUser by viewModel.gitHubUser.collectAsState()
    val gitHubRepos by viewModel.gitHubRepos.collectAsState()
    val gitHubError by viewModel.gitHubError.collectAsState()
    val gitHubLoading by viewModel.gitHubLoading.collectAsState()

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("GITHUB", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Text(gitHubUser?.let { "@$it" } ?: "ربط المستودع", color = DimText, fontSize = 11.sp)
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
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Spacer(modifier = Modifier.height(8.dp))

            if (gitHubUser == null) {
                // Auth screen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("ربط GitHub", color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("أدخل token لربط حسابك مع المشروع.", color = DimText, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = tokenInput,
                            onValueChange = { tokenInput = it },
                            label = { Text("GitHub Token", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                                focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                                cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                            ),
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { if (tokenInput.isNotBlank()) viewModel.connectGitHub(tokenInput) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ربط الحساب", color = VoidBlack, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
                        }

                        gitHubError?.let { error ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(error, color = SignalRed, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            } else {
                // Connected - show repos
                Text("المستودعات", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(12.dp))

                if (gitHubLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = ElectricCyan, trackColor = SteelBorder)
                }

                gitHubRepos.forEach { repo ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                repo.name,
                                color = RawWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "★ ${repo.stargazers_count}",
                                color = AmberAccent,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("المزايا", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    FeatureRow("\u2192", "مزامنة تلقائية مع كل commit")
                    FeatureRow("\u2192", "Pull/Push بدون مغادرة التطبيق")
                    FeatureRow("\u2192", "مراجعة الكود بالذكاء الاصطناعي")
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(icon: String, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(icon, color = ElectricCyan, fontSize = 14.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(end = 8.dp))
        Text(text, color = RawWhite, fontSize = 13.sp)
    }
}
