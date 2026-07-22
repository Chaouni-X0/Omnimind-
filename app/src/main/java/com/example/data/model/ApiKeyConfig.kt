package com.example.omnimind.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_keys")
data class ApiKeyConfig(
    @PrimaryKey val id: String,
    val providerName: String,       // "OpenAI", "Anthropic", "Gemini", "Custom"
    val encryptedKey: String,
    val baseUrl: String?,           // Empty = default provider URL
    @ColumnInfo(defaultValue = "''") val modelId: String = "",
    @ColumnInfo(defaultValue = "1") val modelTier: Int = 1,
    val priorityWeight: Int = 5,    // 1-10 for weighted selection
    val monthlyBudgetCents: Long? = null,
    val currentSpentCents: Long = 0,
    val isEnabled: Boolean = true,
    val lastUsedTimestamp: Long = 0,
    val lastError: String? = null
)
