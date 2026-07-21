package com.example.omnimind.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: String,
    val name: String,
    val localPath: String,
    val status: String,            // "Quiet", "Active", "Needs Decision"
    val progress: Int = 0,         // Percentage: 0 to 100
    val lastUpdated: Long = System.currentTimeMillis(),
    val filesJson: String = "[]",  // List of files in the project
    val apiSpentCents: Long = 0,
    val knowledgeBaseJson: String = "[]" // Knowledge ingestion references
)
