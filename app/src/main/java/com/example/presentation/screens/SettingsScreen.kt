package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.ui.theme.*

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "SYSTEM_PREFERENCES",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textColor = SignalGreen,
                fontSize = 12.sp
            )
            Text(
                text = "SETTINGS",
                fontSize = 54.sp,
                fontWeight = FontWeight.Black,
                textColor = RawWhite,
                fontFamily = FontFamily.Monospace
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            IndustrialSettingsGroup(title = "CORE_INTERFACE") {
                IndustrialSettingsItem(
                    icon = Icons.Default.Terminal,
                    title = "THEME_ENGINE",
                    subtitle = "Cyber-Industrial v1.0"
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            IndustrialSettingsGroup(title = "NETWORK_PROTOCOLS") {
                IndustrialSettingsItem(
                    icon = Icons.Default.Dns,
                    title = "DATABASE_SYNC",
                    subtitle = "SQLITE_REMOTE_ACTIVE"
                )
                IndustrialSettingsItem(
                    icon = Icons.Default.Fingerprint,
                    title = "ENCRYPTION",
                    subtitle = "AES_256_GCM"
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "// OMNIMIND_SYSTEM_STABLE",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                textColor = GhostGrey,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun IndustrialSettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = "// $title",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            textColor = GhostGrey,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SteelBorder)
                .background(IndustrialGrey)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun ColumnScope.IndustrialSettingsItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, textColor = SteelBorder)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = SignalGreen, modifier = Modifier.size(18.dp))
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, 
                textColor = RawWhite, 
                fontWeight = FontWeight.Bold, 
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = subtitle, 
                textColor = GhostGrey, 
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        
        Icon(
            Icons.Default.ChevronRight, 
            contentDescription = null, 
            tint = SteelBorder, 
            modifier = Modifier.size(14.dp)
        )
    }
}
