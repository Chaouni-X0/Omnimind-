package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.model.Project
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    projects: List<Project>,
    onCreateProject: (String) -> Unit,
    onOpenProject: (Project) -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenApiCenter: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onOpenProfile: () -> Unit = {}
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var projectName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = VoidBlack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = ElectricCyan,
                contentColor = VoidBlack,
                shape = RoundedCornerShape(12.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "New Project") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("OMNIMIND", style = MaterialTheme.typography.labelLarge, color = ElectricCyan, letterSpacing = 3.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("لوحة التحكم", style = MaterialTheme.typography.headlineLarge, color = RawWhite, fontWeight = FontWeight.ExtraBold)
                Text("إدارة مشاريعك والوكلاء", color = DimText, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickAction(Icons.Filled.PlayArrow, "GitHub", ElectricCyan, modifier = Modifier.weight(1f).clickable { onOpenGitHub() })
                    QuickAction(Icons.Filled.Api, "API Center", AmberAccent, modifier = Modifier.weight(1f).clickable { onOpenApiCenter() })
                    QuickAction(Icons.Filled.Settings, "الإعدادات", DimText, modifier = Modifier.weight(1f).clickable { onOpenSettings() })
                    QuickAction(Icons.Filled.Person, "الملف", MutedGrey, modifier = Modifier.weight(1f).clickable { onOpenProfile() })
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatsBar(projectCount = projects.size, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("المشاريع", style = MaterialTheme.typography.titleMedium, color = RawWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Text("${projects.size} مشروع", color = DimText, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (projects.isEmpty()) {
                item { EmptyProjectsCard() }
            } else {
                items(projects, key = { it.id }) { project ->
                    ProjectCard(project) { onOpenProject(project) }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false; projectName = "" },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(16.dp),
            title = { Text("مشروع جديد", color = RawWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
            text = {
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = { Text("اسم المشروع", fontSize = 12.sp, fontFamily = FontFamily.Monospace) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricCyan, unfocusedBorderColor = SteelBorder,
                        focusedLabelColor = ElectricCyan, unfocusedLabelColor = DimText,
                        cursorColor = ElectricCyan, focusedTextColor = RawWhite, unfocusedTextColor = RawWhite
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { if (projectName.isNotBlank()) { onCreateProject(projectName); showCreateDialog = false; projectName = "" } }) {
                    Text("إنشاء", color = ElectricCyan, fontFamily = FontFamily.Monospace)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("إلغاء", color = DimText) }
            }
        )
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = label, tint = accent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = RawWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
private fun StatsBar(projectCount: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(colors = listOf(SurfaceDark, SurfaceElevated)))
            .border(0.5.dp, SteelBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("مشاريع", projectCount.toString(), ElectricCyan)
        Spacer(modifier = Modifier.width(16.dp))
        StatItem("مزودين", "5+", AmberAccent)
        Spacer(modifier = Modifier.width(16.dp))
        StatItem("التكلفة", "0$", SignalGreen)
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, fontFamily = FontFamily.Monospace)
        Text(text = label, color = DimText, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun EmptyProjectsCard() {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, SteelBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "\uD83D\uDE80", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "لا توجد مشاريع بعد", color = RawWhite, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "أنشئ مشروعك الأول وابدأ البناء مع AI", color = DimText, fontSize = 13.sp)
        }
    }
}

@Composable
private fun ProjectCard(project: Project, onClick: () -> Unit) {
    val statusColor = when (project.status) {
        "Active" -> SignalGreen
        "Quiet" -> DimText
        "Needs Decision" -> AmberAccent
        else -> DimText
    }
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(ElectricCyan.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Code, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = project.name, color = RawWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(statusColor, RoundedCornerShape(3.dp)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = project.status, color = statusColor, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
            }
            Icon(Icons.Filled.PlayArrow, contentDescription = "Open", tint = DimText, modifier = Modifier.size(20.dp))
        }
        if (project.progress > 0) {
            LinearProgressIndicator(
                progress = project.progress / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = ElectricCyan,
                trackColor = SteelBorder
            )
        }
    }
}
