package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProjectWorkspaceScreen(
    projectName: String,
    onOpenTerminal: () -> Unit,
    onOpenEditor: () -> Unit,
    onOpenChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = projectName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onOpenChat, modifier = Modifier.fillMaxWidth()) {
            Text("دردشة الوكلاء")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onOpenEditor, modifier = Modifier.fillMaxWidth()) {
            Text("محرر الأكواد")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onOpenTerminal, modifier = Modifier.fillMaxWidth()) {
            Text("الترمنال")
        }
    }
}
