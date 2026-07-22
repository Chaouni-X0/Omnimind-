package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.model.Project
import com.example.omnimind.presentation.components.AgentStatusBadge
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    projects: List<Project>,
    onCreateProject: (name: String) -> Unit,
    onOpenProject: (Project) -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenApiCenter: () -> Unit
) {
    var newProjectName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = VoidBlack,
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Top Nav - Minimalist
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "OMNIMIND_v1.0", 
                        fontFamily = FontFamily.Monospace,
                        color = SignalGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Row {
                        IconButton(onClick = onOpenApiCenter) {
                            Icon(Icons.Filled.Terminal, contentDescription = null, tint = RawWhite)
                        }
                        IconButton(onClick = onOpenGitHub) {
                            Icon(Icons.Filled.Settings, contentDescription = null, tint = RawWhite)
                        }
                    }
                }

                // Huge Bold Typography
                Text(
                    text = "SYSTEM\nOPERATIONS",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Black,
                    color = RawWhite,
                    lineHeight = 48.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Asymmetric Input Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SteelBorder)
                        .background(IndustrialGrey)
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "NEW_DEPLOYMENT",
                            fontFamily = FontFamily.Monospace,
                            color = SignalGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = newProjectName,
                            onValueChange = { newProjectName = it },
                            placeholder = { Text("ID: PROJECT_NAME", color = GhostGrey, fontFamily = FontFamily.Monospace) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = SignalGreen,
                                unfocusedIndicatorColor = SteelBorder,
                                textColor = RawWhite
                            )
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                if (newProjectName.isNotBlank()) {
                                    onCreateProject(newProjectName)
                                    newProjectName = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End),
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = SignalGreen)
                        ) {
                            Text("INITIALIZE", color = VoidBlack, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))
                
                Text(
                    text = "// ACTIVE_INSTANCES",
                    fontFamily = FontFamily.Monospace,
                    color = GhostGrey,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (projects.isEmpty()) {
                    Text(
                        "NULL_RECORDS_FOUND",
                        color = GhostGrey,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(1.dp), // Brutalist gap
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(projects, key = { it.id }) { project ->
                            IndustrialProjectCard(project, onClick = { onOpenProject(project) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IndustrialProjectCard(project: Project, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SteelBorder)
            .background(IndustrialGrey)
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    color = RawWhite,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "PROG: ${project.progress}%",
                        color = SignalGreen,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .weight(1f)
                            .background(SteelBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(project.progress / 100f)
                                .fillMaxHeight()
                                .background(SignalGreen)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            AgentStatusBadge(status = project.status)
        }
    }
}
