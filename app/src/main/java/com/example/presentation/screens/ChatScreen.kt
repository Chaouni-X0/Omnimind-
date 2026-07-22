package com.example.omnimind.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RectangleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omnimind.data.model.AgentMessage
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
            .background(VoidBlack)
            .drawWithContent {
                drawContent()
                // Scanline Effect
                val scanlineSpacing = 8.dp.toPx()
                for (y in 0..size.height.toInt() step scanlineSpacing.toInt()) {
                    drawLine(
                        textColor = ScanlineColor,
                        start = Offset(0f, y.toFloat()),
                        end = Offset(size.width, y.toFloat()),
                        strokeWidth = 1f
                    )
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Asymmetric Header
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "TERMINAL_01",
                style = MaterialTheme.typography.labelSmall,
                color = SignalGreen,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp).width(40.dp),
                color = SignalGreen,
                thickness = 2.dp
            )
            Text(
                text = "OMNIMIND_CORE",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = RawWhite,
                lineHeight = 40.sp,
                fontFamily = FontFamily.Monospace
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Messages List with Raw Industrial Style
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    IndustrialMessageBubble(message)
                }
            }
        }

        // Brutalist Input Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(VoidBlack)
                .border(width = 1.dp, textColor = SteelBorder, shape = RectangleShape)
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ">",
                    color = SignalGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp)
                )
                TextField(
                    value = taskInput,
                    onValueChange = { taskInput = it },
                    placeholder = { 
                        Text(
                            "INITIATE_COMMAND...", 
                            color = GhostGrey,
                            fontFamily = FontFamily.Monospace
                        ) 
                    },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = SignalGreen,
                        color = RawWhite
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
                        .border(1.dp, if (taskInput.isNotBlank()) SignalGreen else SteelBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "EXECUTE",
                        tint = if (taskInput.isNotBlank()) SignalGreen else SteelBorder
                    )
                }
            }
        }
    }
}

@Composable
private fun IndustrialMessageBubble(message: AgentMessage) {
    val isAgent = message.agentName != "User"
    
    val accentColor = when (message.verdictType) {
        "APPROVE" -> SignalGreen
        "REJECT", "VETO" -> SignalRed
        else -> SignalGreen
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isAgent) Alignment.Start else Alignment.End
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isAgent) {
                Box(modifier = Modifier.size(6.dp).background(accentColor))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = message.agentName.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                textColor = if (isAgent) accentColor else GhostGrey,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(if (isAgent) IndustrialGrey else Color.Transparent)
                .border(
                    width = 1.dp,
                    textColor = if (isAgent) SteelBorder else SignalGreenDim
                )
                .padding(16.dp)
        ) {
            Text(
                text = message.messageText,
                textColor = if (isAgent) RawWhite else SignalGreen,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                lineHeight = 20.sp
            )
        }
        
        if (message.verdictType != "NONE") {
            Text(
                text = "[STATUS: ${message.verdictType}]",
                textColor = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp),
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
