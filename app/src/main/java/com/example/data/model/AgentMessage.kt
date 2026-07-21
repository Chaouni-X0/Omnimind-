package com.example.omnimind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_messages")
data class AgentMessage(
    @PrimaryKey val id: String,
    val taskId: String,
    val agentId: String,           // e.g. "Architect", "Analyst", "Coder", "Tester", "Guardian"
    val agentName: String,
    val messageText: String,
    val thinking: String? = null,  // For Manus-style thinking block
    val actions: String? = null,   // For tool calls / actions
    val output: String? = null,    // For final structured output
    val verdictType: String,       // "APPROVE", "REJECT", "VETO", "NONE"
    val verdictReason: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
