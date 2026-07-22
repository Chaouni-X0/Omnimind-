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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.R
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
        containerColor = ManusBlack,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "OMNIMIND", 
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = Color.White
                    ) 
                },
                actions = {
                    IconButton(onClick = onOpenApiCenter) {
                        Icon(Icons.Filled.Hub, contentDescription = "API Center", tint = Color.White)
                    }
                    IconButton(onClick = onOpenGitHub) {
                        Icon(Icons.Filled.Settings, contentDescription = "GitHub", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Gradient Glow
            Box(
                modifier = Modifier
                    .size(500.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-150).dp, y = 150.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(ManusElectricBlueGlow, Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Create Project Card (Manus Style)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ManusSurface)
                        .border(1.dp, ManusBorder, RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "START NEW PROJECT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = ManusElectricBlue,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = newProjectName,
                            onValueChange = { newProjectName = it },
                            placeholder = { Text("What are we building today?", color = ManusTextSecondary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = ManusElectricBlue,
                                unfocusedIndicatorColor = ManusBorder,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (newProjectName.isNotBlank()) {
                                    onCreateProject(newProjectName)
                                    newProjectName = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ManusElectricBlue)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Initialize Engine", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "ACTIVE WORKSPACES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ManusTextSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (projects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(ManusSurface)
                            .border(1.dp, ManusBorder, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No active projects found.",
                            color = ManusTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(projects, key = { it.id }) { project ->
                            ProjectCard(project, onClick = { onOpenProject(project) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectCard(project: Project, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ManusSurface)
            .border(1.dp, ManusBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = project.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(4.dp)
                        .clip(CircleShape),
                    color = ManusElectricBlue,
                    trackColor = ManusBorder
                )
            }
            AgentStatusBadge(status = project.status)
        }
    }
}
