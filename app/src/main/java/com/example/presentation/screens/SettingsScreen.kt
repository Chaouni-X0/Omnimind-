package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.ui.theme.*

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ManusBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "SETTINGS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = ManusElectricBlue,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            SettingsGroup(title = "Appearance") {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Theme",
                    subtitle = "Manus Obsidian (Default)"
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            SettingsGroup(title = "System") {
                SettingsItem(
                    icon = Icons.Default.Storage,
                    title = "Database",
                    subtitle = "Local SQLite Sync"
                )
                SettingsItem(
                    icon = Icons.Default.Security,
                    title = "Security",
                    subtitle = "End-to-end Encryption"
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "OmniMind v1.0.0-Manus",
                style = MaterialTheme.typography.labelSmall,
                color = ManusTextSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(ManusSurface)
                .border(1.dp, ManusBorder, RoundedCornerShape(20.dp))
        ) {
            Column { content() } {
                // Items will be added here
            }
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(GlassWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = ManusElectricBlue, modifier = Modifier.size(20.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(text = subtitle, color = ManusTextSecondary, fontSize = 12.sp)
        }
        
        Icon(
            Icons.Default.ArrowForwardIos, 
            contentDescription = null, 
            tint = ManusBorder, 
            modifier = Modifier.size(12.dp)
        )
    }
}
