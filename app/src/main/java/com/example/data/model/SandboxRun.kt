package com.example.omnimind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sandbox_runs")
data class SandboxRun(
    @PrimaryKey val id: String,
    val taskId: String,
    val buildSuccess: Boolean,
    val testsSuccess: Boolean,
    val compileLog: String,
    val testLog: String,
    val diffSummary: String,
    val isApplied: Boolean = false,
    val isIgnored: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
