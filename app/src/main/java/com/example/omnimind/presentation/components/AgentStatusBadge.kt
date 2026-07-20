package com.example.omnimind.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AgentStatusBadge(status: String) {
    val color = when (status.lowercase()) {
        "active" -> Color.Green
        "inactive" -> Color.Red
        else -> Color.Gray
    }
    
    Box(modifier = Modifier
        .background(color)
        .padding(8.dp)) {
        Text(text = status, color = Color.White)
    }
}