package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.model.AgentMessage
import com.example.omnimind.ui.theme.*
import com.example.omnimind.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    messages: List<AgentMessage>,
    onSendTask: (title: String, description: String) -> Unit
) {
    var taskInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ManusBlack)
    ) {
        // Background Glow Effect
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(ManusElectricBlueGlow, Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OmniMind",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ManusElectricBlue)
                )
            }

            // Messages List
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    AgentMessageBubble(message)
                }
            }
        }

        // Floating Input Bar (Manus Style)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(GlassWhite)
                .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    placeholder = { 
                        Text(
                            "Ask OmniMind anything...", 
                            color = ManusTextSecondary,
                            fontSize = 15.sp
                        ) 
                    },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = ManusElectricBlue,
                        color = Color.White
                    )
                )
                
                IconButton(
                    onClick = {
                        if (taskInput.isNotBlank()) {
                            onSendTask(taskInput.take(60), taskInput)
                            taskInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (taskInput.isNotBlank()) ManusElectricBlue else GlassWhiteHeavy)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AgentMessageBubble(message: AgentMessage) {
    val isAgent = message.agentName != "User" // Assuming "User" for user messages
    
    val accentColor = when (message.verdictType) {
        "APPROVE" -> Color(0xFF4CAF50)
        "REJECT", "VETO" -> Color(0xFFFF5252)
        else -> ManusElectricBlue
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isAgent) Alignment.Start else Alignment.End
    ) {
        if (isAgent) {
            Text(
                text = message.agentName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                letterSpacing = 1.sp
            )
        }
        
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isAgent) 4.dp else 20.dp,
                        bottomEnd = if (isAgent) 20.dp else 4.dp
                    )
                )
                .background(if (isAgent) ManusSurface else ManusElectricBlue)
                .border(
                    width = 1.dp,
                    color = if (isAgent) ManusBorder else Color.Transparent,
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isAgent) 4.dp else 20.dp,
                        bottomEnd = if (isAgent) 20.dp else 4.dp
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.messageText,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }
    }
}
