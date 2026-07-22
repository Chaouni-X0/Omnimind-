package com.example.omnimind.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBuilderScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "App Builder")
        Text(text = "Build your custom AI workflow")
    }
}