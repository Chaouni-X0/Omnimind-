package com.example.omnimind.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.ui.theme.*

@Composable
fun AgentStatusBadge(status: String) {
    val (color, label) = when (status.lowercase()) {
        "active", "running", "processing" -> SignalGreen to "RUNNING"
        "idle", "waiting" -> Color(0xFFFFCC00) to "STANDBY"
        "completed", "done" -> RawWhite to "STABLE"
        "error", "failed" -> SignalRed to "CRITICAL"
        else -> GhostGrey to status.uppercase()
    }
    
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = color, shape = RectangleShape)
            .background(color.copy(alpha = 0.05f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )
    }
}
