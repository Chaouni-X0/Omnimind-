package com.example.omnimind.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.ui.theme.*

@Composable
fun AgentStatusBadge(status: String) {
    val (color, label) = when (status.lowercase()) {
        "active", "running", "processing" -> ManusElectricBlue to "ACTIVE"
        "idle", "waiting" -> Color(0xFFFFA000) to "IDLE"
        "completed", "done" -> Color(0xFF4CAF50) to "SYNCED"
        "error", "failed" -> Color(0xFFFF5252) to "ERROR"
        else -> ManusTextSecondary to status.uppercase()
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}
