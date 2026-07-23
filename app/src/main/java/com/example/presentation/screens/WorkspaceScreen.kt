package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    projectId: String,
    projectName: String,
    onOpenTerminal: () -> Unit,
    onOpenEditor: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenApiCenter: () -> Unit,
    onOpenProfile: () -> Unit,
    onBack: () -> Unit
) {
    var selectedPanel by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = VoidBlack,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            projectName,
                            color = RawWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            "Active • 0%",
                            color = DimText,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DimText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        },
        bottomBar = {
            WorkspaceBottomNav(
                selectedPanel,
                onPanelSelected = { selectedPanel = it },
                onOpenChat = onOpenChat,
                onOpenTerminal = onOpenTerminal,
                onOpenEditor = onOpenEditor,
                onOpenGitHub = onOpenGitHub,
                onOpenSettings = onOpenSettings,
                onOpenApiCenter = onOpenApiCenter,
                onOpenProfile = onOpenProfile
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedPanel) {
                0 -> OverviewPanel(projectName)
                1 -> LogsPanel()
                else -> OverviewPanel(projectName)
            }
        }
    }
}

@Composable
private fun WorkspaceBottomNav(
    selectedPanel: Int,
    onPanelSelected: (Int) -> Unit,
    onOpenChat: () -> Unit,
    onOpenTerminal: () -> Unit,
    onOpenEditor: () -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenApiCenter: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark)
            .border(0.5.dp, SteelBorder)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val tabs = listOf(
            Icons.Filled.Hub to "Overview" to onPanelSelected::invoke,
            Icons.Filled.Terminal to "Terminal" to null,
            Icons.Filled.Code to "Editor" to null,
            Icons.Filled.Hub to "GitHub" to null
        )
        // Overview tab
        BottomNavItem(Icons.Filled.Hub, "Overview", selectedPanel == 0) { onPanelSelected(0) }
        // Terminal
        BottomNavItem(Icons.Filled.Terminal, "Terminal", false) { onOpenTerminal() }
        // Editor
        BottomNavItem(Icons.Filled.Code, "Editor", false) { onOpenEditor() }
        // Chat
        BottomNavItem(Icons.Filled.Hub, "Chat", false) { onOpenChat() }
        // GitHub
        BottomNavItem(Icons.Filled.Hub, "GitHub", false) { onOpenGitHub() }
    }
}

@Composable
private fun BottomNavItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) ElectricCyan.copy(alpha = 0.1f) else Color.Transparent)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (selected) ElectricCyan else DimText,
            modifier = Modifier.size(18.dp)
        )
        Text(
            label,
            color = if (selected) ElectricCyan else DimText,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun OverviewPanel(projectName: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("معلومات المشروع", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow("الاسم", projectName, RawWhite)
                    InfoRow("الحالة", "Active", SignalGreen)
                    InfoRow("التقدم", "0%", AmberAccent)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("الوكلاء النشطون", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    val agents = listOf(
                        "Planner" to "Active",
                        "Coder" to "Idle",
                        "Estimator" to "Idle",
                        "Verifier" to "Idle"
                    )
                    agents.forEach { (name, status) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(if (status == "Active") SignalGreen else MutedGrey))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(name, color = RawWhite, fontSize = 13.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f))
                            Text(status, color = if (status == "Active") SignalGreen else DimText, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, SteelBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("إحصائيات التكلفة", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow("إجمالي الاستخدام", "0.00$", SignalGreen)
                    InfoRow("حد الميزانية", "5.00$", DimText)
                    InfoRow("التوفير", "~95%", SignalGreen)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = DimText, fontSize = 12.sp, modifier = Modifier.weight(1f))
        Text(value, color = color, fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LogsPanel() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("سجل العمليات", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(12.dp))

        val logs = listOf(
            "[${java.util.Date().toString().take(12)}] نظام جاهز للعمل",
            "[${java.util.Date().toString().take(12)}] الوكلاء متصلون",
            "[${java.util.Date().toString().take(12)}] في انتظار الأوامر...",
        )

        logs.forEach { log ->
            Text(log, color = DimText, fontSize = 11.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}
