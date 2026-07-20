package com.example.omnimind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_tasks")
data class AgentTask(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val prompt: String,
    val status: String,            // "Pending", "Planning", "Coding", "Testing", "Review", "Completed", "Vetoed"
    val progress: Int = 0,         // Percentage: 0 to 100
    val costEstimateCents: Long = 0,
    val timeEstimateSeconds: Long = 0,
    val isApprovedByEstimator: Boolean = false,
    val filesModifiedCount: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)
